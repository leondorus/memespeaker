package me.leondorus.memespeaker.savers

import me.leondorus.memespeaker.repos.MessageRepo

class MessageSaver(
    private val messageUpdateRepo: MessageUpdateRepo,
    private val messageRepo: MessageRepo
) {
    suspend fun startSaving() {
        messageUpdateRepo.getMessageUpdates().collect { mess ->
            messageRepo.addMessage(mess)
        }
    }
}