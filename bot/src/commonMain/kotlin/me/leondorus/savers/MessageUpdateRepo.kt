package me.leondorus.savers

import kotlinx.coroutines.flow.Flow
import me.leondorus.data.Message

interface MessageUpdateRepo {
    fun getMessageUpdates(): Flow<Message>
}