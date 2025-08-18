package me.leondorus.room.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.leondorus.data.INT_TO_TOPIC
import me.leondorus.data.Message
import me.leondorus.data.TOPIC
import me.leondorus.data.mid

@Entity(tableName = "messages")
data class RoomMessage(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val topic: Int?,
    val wasPromoted: Boolean
)

fun RoomMessage.toMessage() = Message(id.mid, INT_TO_TOPIC[topic]!!, wasPromoted)
fun Message.toRoomMessage() = RoomMessage(id.id, topic.id, wasPromoted)
