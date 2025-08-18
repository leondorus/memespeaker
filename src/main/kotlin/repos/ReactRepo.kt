package me.leondorus.repos

import kotlinx.coroutines.flow.Flow
import me.leondorus.data.MessageId
import me.leondorus.data.React

interface ReactRepo {
    suspend fun addReact(react: React)
    suspend fun delReact(react: React)

    fun getReactsForMessage(messageId: MessageId): Flow<List<React>>
    fun getReactsAddUpdates(): Flow<React>
}