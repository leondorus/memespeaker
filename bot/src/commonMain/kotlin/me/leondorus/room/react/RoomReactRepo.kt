package me.leondorus.room.react

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import me.leondorus.data.MessageId
import me.leondorus.data.React
import me.leondorus.repos.ReactRepo

class RoomReactRepo(private val dao: ReactDao) : ReactRepo {
    private val reactFlow = MutableSharedFlow<React>()

    override suspend fun addReact(react: React) {
        val res = dao.insert(react.toRoomReact())
        if (res >= 0L)
            reactFlow.emit(react)
    }

    override suspend fun delReact(react: React) {
        dao.delete(react.toRoomReact())
    }

    override fun getReactsForMessage(messageId: MessageId): Flow<List<React>> =
        dao.getByMessageId(messageId.id).map { rs -> rs.map { r -> r.toReact() } }

    override fun getReactsAddUpdates() = reactFlow as Flow<React>
}