package me.leondorus.room.react

import androidx.room.Entity
import me.leondorus.data.React
import me.leondorus.data.mid
import me.leondorus.data.uid

@Entity(
    tableName = "reacts",
    primaryKeys = ["messageId", "userId"],
//    foreignKeys = [
//        ForeignKey(
//            entity = RoomMessage::class,
//            parentColumns = ["id"],
//            childColumns = ["messageId"],
//            onDelete = ForeignKey.CASCADE
//        )]
)
data class RoomReact(
    val react: String,
    val messageId: Long,
    val userId: Long
)

fun RoomReact.toReact() = React(react, messageId.mid, userId.uid)
fun React.toRoomReact() = RoomReact(react, messageId.id, userId.id)

