package me.leondorus.memespeaker.tgbot.ktortg

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.mid
import me.leondorus.memespeaker.data.uid
import me.leondorus.memespeaker.savers.MessageUpdateRepo
import me.leondorus.memespeaker.savers.ReactUpdate
import me.leondorus.memespeaker.savers.ReactUpdateRepo
import me.leondorus.memespeaker.tgbot.ktortg.data.TgMessage
import me.leondorus.memespeaker.tgbot.ktortg.data.TgMessageReactionUpdated
import me.leondorus.memespeaker.tgbot.ktortg.data.toMessage
import me.leondorus.memespeaker.tgbot.ktortg.data.toReact

class UpdateRepo(private val chatId: Long, private val ktorBot: KtorBot, private val scope: CoroutineScope) :
    ReactUpdateRepo, MessageUpdateRepo {
    private val reactUpdateFlow = MutableSharedFlow<ReactUpdate>()
    private val messageUpdateFlow = MutableSharedFlow<Message>()
    private var updateCollectingJob: Job? = null

    override fun getReactUpdates(): Flow<ReactUpdate> {
        startCollectingUpdates()
        return reactUpdateFlow
    }

    override fun getMessageUpdates(): Flow<Message> {
        startCollectingUpdates()
        return messageUpdateFlow
    }

    private fun startCollectingUpdates() {
        if (updateCollectingJob != null)
            return

        updateCollectingJob = scope.launch {
            ktorBot.getUpdates().collect { u ->
                when {
                    (u.message != null) -> handleMessage(u.message)
                    (u.message_reaction != null) -> handleReactUpdate(u.message_reaction)
                }
            }
        }
    }

    private suspend fun handleMessage(tgMessage: TgMessage) {
        if (tgMessage.chat.id != chatId)
            return

        messageUpdateFlow.emit(tgMessage.toMessage())
    }

    private suspend fun handleReactUpdate(tgReactUpdate: TgMessageReactionUpdated) {
        if (tgReactUpdate.chat.id != chatId)
            return

        val userId = tgReactUpdate.user?.id?.uid
        if (userId == null)
        // TODO: User is anonymous, maybe should handle this case somehow
            return
        val messageId = tgReactUpdate.message_id.mid

        val oldReacts = tgReactUpdate.old_reaction.map { rt -> rt.toReact(messageId, userId) }
        val newReacts = tgReactUpdate.new_reaction.map { rt -> rt.toReact(messageId, userId) }
        val reactUpdate = ReactUpdate(oldReacts, newReacts)

        reactUpdateFlow.emit(reactUpdate)
    }
}