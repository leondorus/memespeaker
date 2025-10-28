package me.leondorus.memespeaker.promote

import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.MessageId
import me.leondorus.memespeaker.data.TOPIC

interface Forwarder {
    // Forwards message to this topic. Returns new message
    suspend fun forwardMessageToTopic(messageId: MessageId, topic: TOPIC): Message
}