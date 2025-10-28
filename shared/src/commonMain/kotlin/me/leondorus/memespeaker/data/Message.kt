package me.leondorus.memespeaker.data

data class Message(
    val id: MessageId,
    val topic: TOPIC,
    val wasPromoted: Boolean
)