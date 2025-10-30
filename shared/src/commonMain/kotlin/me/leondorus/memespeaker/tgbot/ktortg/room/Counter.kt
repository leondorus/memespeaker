package me.leondorus.memespeaker.tgbot.ktortg.room

import androidx.room.Entity

@Entity(
    tableName = "counters",
    primaryKeys = ["id"]
)
data class Counter(
    val id: String,
    val value: Int
)