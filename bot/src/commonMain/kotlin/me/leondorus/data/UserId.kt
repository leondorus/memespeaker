package me.leondorus.data

@JvmInline
value class UserId(val id: Int)

val Int.uid: UserId
    get() = UserId(this)