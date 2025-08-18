package me.leondorus.promote

import me.leondorus.data.Message
import me.leondorus.data.MessageId
import me.leondorus.data.TOPIC

interface Forwarder {
    // Forwards message to this topic. Returns new message
    suspend fun forwardMessageToTopic(messageId: MessageId, topic: TOPIC): Message
}