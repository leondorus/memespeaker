package me.leondorus.data

@JvmInline
value class MessageId(val id: Int)

val Int.mid: MessageId
    get() = MessageId(this)