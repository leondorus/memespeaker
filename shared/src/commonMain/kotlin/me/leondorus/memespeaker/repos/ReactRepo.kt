package me.leondorus.memespeaker.repos

import kotlinx.coroutines.flow.Flow
import me.leondorus.memespeaker.data.MessageId
import me.leondorus.memespeaker.data.React

interface ReactRepo {
    suspend fun addReact(react: React)
    suspend fun delReact(react: React)

    fun getReactsForMessage(messageId: MessageId): Flow<List<React>>
    fun getReactsAddUpdates(): Flow<React>
}