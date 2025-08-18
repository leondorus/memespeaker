package me.leondorus.room.message

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.leondorus.data.Message
import me.leondorus.data.MessageId
import me.leondorus.repos.MessageRepo

class RoomMessageRepo(private val dao: MessageDao) : MessageRepo {
    override suspend fun addMessage(message: Message) {
        dao.insert(message.toRoomMessage())
    }

    override suspend fun promoteMessage(messageId: MessageId): Boolean = dao.promote(messageId.id)

    override fun getMessage(messageId: MessageId): Flow<Message?> = dao.findById(messageId.id).map { it?.toMessage() }

    override fun getAllMessages(): Flow<List<Message>> = dao.getAll().map { rms -> rms.map { rm -> rm.toMessage() } }
}