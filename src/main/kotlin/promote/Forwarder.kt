package me.leondorus.promote

import me.leondorus.data.MessageId
import me.leondorus.data.TOPIC

interface Forwarder {
    // Forwards message to this topic and updates state if needed
    suspend fun forwardMessageToTopic(messageId: MessageId, topic: TOPIC)
}