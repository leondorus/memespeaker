package me.leondorus.memespeaker.savers

import kotlinx.coroutines.flow.Flow
import me.leondorus.memespeaker.data.Message

interface MessageUpdateRepo {
    fun getMessageUpdates(): Flow<Message>
}