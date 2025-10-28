package me.leondorus.memespeaker.tgbot.ktortg

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import me.leondorus.memespeaker.tgbot.ktortg.data.TgMessage
import me.leondorus.memespeaker.tgbot.ktortg.data.TgUpdate
import kotlin.math.max

private const val telegramUrl = "https://api.telegram.org/bot"
private const val longPollingTimeout = 6L

class KtorBot(private val token: String, private val scope: CoroutineScope) {
    private val client = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }

    private val updateFlow: MutableSharedFlow<TgUpdate> = MutableSharedFlow()
    private var updateJob: Job? = null
    private var maxSeenUpdateId = 175144188

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
                "offset" to (maxSeenUpdateId + 1).toString(),
                "timeout" to longPollingTimeout.toString(),
                "allowed_updates" to """["message","message_reaction"]"""
            )
        )

        val updateJson = performRequestAndReturnJson(requestUrl)
        val updates = json.decodeFromJsonElement<List<TgUpdate>>(updateJson)
        val maxCurUpdateId = updates.maxByOrNull { u -> u.update_id }?.update_id ?: Int.MIN_VALUE
        maxSeenUpdateId = max(maxSeenUpdateId, maxCurUpdateId)

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
}