package me.leondorus.repos

import kotlinx.coroutines.flow.Flow
import me.leondorus.data.Message
import me.leondorus.data.MessageId

interface MessageRepo {
    suspend fun addMessage(message: Message)

    // Performs transaction to me.leondorus.promote message
    // Returns true if it was the first time that this message got promoted
    // Returns false if it was already promoted
    suspend fun promoteMessage(messageId: MessageId): Boolean

    fun getMessage(messageId: MessageId): Flow<Message?>
    fun getAllMessages(): Flow<List<Message>>
}