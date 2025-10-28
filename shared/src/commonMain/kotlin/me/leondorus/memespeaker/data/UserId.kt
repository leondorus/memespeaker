package me.leondorus.memespeaker.data

@JvmInline
value class UserId(val id: Long)

val Long.uid: UserId
    get() = UserId(this)