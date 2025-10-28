package me.leondorus.memespeaker.room.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.mid
import me.leondorus.memespeaker.data.tpc

@Entity(tableName = "messages")
data class RoomMessage(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val topic: Long?,
    val wasPromoted: Boolean
)

fun RoomMessage.toMessage() = Message(id.mid, topic.tpc!!, wasPromoted)
fun Message.toRoomMessage() = RoomMessage(id.id, topic.id, wasPromoted)
