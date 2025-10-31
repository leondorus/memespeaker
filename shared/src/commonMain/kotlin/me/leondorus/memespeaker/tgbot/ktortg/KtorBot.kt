package me.leondorus.memespeaker.tgbot.ktortg

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import me.leondorus.memespeaker.tgbot.ktortg.data.TgMessage
import me.leondorus.memespeaker.tgbot.ktortg.data.TgUpdate
import me.leondorus.memespeaker.tgbot.ktortg.room.Counter
import me.leondorus.memespeaker.tgbot.ktortg.room.CounterDao

private const val telegramUrl = "https://api.telegram.org/bot"
private const val longPollingTimeout = 6L

class KtorBot(private val token: String, private val scope: CoroutineScope, private val counterDao: CounterDao) {
    private val client = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }

    private val updateFlow: MutableSharedFlow<TgUpdate> = MutableSharedFlow()
    private var updateJob: Job? = null

    suspend fun forwardMessage(
        chatId: Long,
        fromChatId: Long,
        messageId: Long,
        messageThreadId: Long?,
    ): TgMessage {
        val request = getRequestUrl(
            "forwardMessage", mapOf(
                "chat_id" to chatId.toString(),
                "from_chat_id" to fromChatId.toString(),
                "message_id" to messageId.toString(),
                "message_thread_id" to messageThreadId?.toString(),
            )
        )

        val jsonResult = performRequestAndReturnJson(request)
        val mess = json.decodeFromJsonElement<TgMessage>(jsonResult)
        return mess
    }

    fun getUpdates(): Flow<TgUpdate> {
        initUpdateJob(scope)
        return updateFlow
    }

    private fun initUpdateJob(scope: CoroutineScope) {
        if (updateJob != null)
            return

        updateJob = scope.launch {
            while (true) {
                // I think I don't need delay, because of the long polling.
                // Long polling will wait for some timeout amount of seconds,
                // but if it gets some updates it will return immediately
                // delay(delayInSeconds*1000)

                val updates = getCurrentUpdates()
                for (u in updates)
                    updateFlow.emit(u)
            }
        }
    }

    suspend fun getCurrentUpdates(): List<TgUpdate> {
        val requestUrl = getRequestUrl(
            "getUpdates", mapOf(
                "offset" to (getMaxSeenUpdateId() + 1).toString(),
                "timeout" to longPollingTimeout.toString(),
                "allowed_updates" to """["message","message_reaction"]"""
            )
        )

        val updateJson: JsonElement
        try {
            updateJson = performRequestAndReturnJson(requestUrl)
        } catch (e: IOException) {
            // TODO: Log it
            return listOf()
        }
        val updates = json.decodeFromJsonElement<List<TgUpdate>>(updateJson)
        val receivedUpdateId = updates.maxByOrNull { u -> u.update_id }?.update_id ?: Int.MIN_VALUE
        updateMaxSeenUpdateId(receivedUpdateId)

        return updates
    }

    private suspend fun performRequestAndReturnJson(request: String): JsonElement {
        val response = client.get(request)
        if (!response.status.isSuccess()) {
            throw RuntimeException("Request was not successive, status: ${response.status}")
        }

        val text: String = response.body()
        val data = json.parseToJsonElement(text) as JsonObject
        val jsonElem = data["result"] as JsonElement

        return jsonElem
    }

    private fun getRequestUrl(requestName: String, map: Map<String, String?>): String {
        val args = map.filter { (_, v) -> v != null }.map { (k, v) -> "$k=$v" }
        val argsStr = args.joinToString("&")
        val res = "$telegramUrl$token/$requestName?$argsStr"
        return res
    }


    private val counterId = token.substring(0, 8)
    private suspend fun getMaxSeenUpdateId(): Int = counterDao.getCounter(counterId)?.value ?: 0
    private suspend fun updateMaxSeenUpdateId(newCounter: Int) = counterDao.maxCounter(Counter(counterId, newCounter))
}