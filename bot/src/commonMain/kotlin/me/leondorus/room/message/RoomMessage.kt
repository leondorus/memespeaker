package me.leondorus.room.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.leondorus.data.Message
import me.leondorus.data.mid
import me.leondorus.data.tpc

@Entity(tableName = "messages")
data class RoomMessage(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val topic: Long?,
    val wasPromoted: Boolean
)

fun RoomMessage.toMessage() = Message(id.mid, topic.tpc!!, wasPromoted)
fun Message.toRoomMessage() = RoomMessage(id.id, topic.id, wasPromoted)
