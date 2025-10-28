package me.leondorus.data

@JvmInline
value class MessageId(val id: Long)

val Long.mid: MessageId
    get() = MessageId(this)