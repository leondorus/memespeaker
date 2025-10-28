package me.leondorus.tgbot.ktortg.data

import kotlinx.serialization.Serializable
import me.leondorus.data.Message
import me.leondorus.data.mid
import me.leondorus.data.tpc

@Serializable
@Suppress("PropertyName")
data class TgMessage(
    val message_id: Long,
    val message_thread_id: Int? = null,
    val from: TgUser? = null,
    val sender_chat: TgChat? = null,
//    val sender_boost_count: Int? = null,
//    val sender_business_bot: TgUser? = null,
    val date: TgDate,
//    val business_connection_id: String? = null,
    val chat: TgChat,
    val forward_origin: TgMessageOrigin? = null,
    val is_topic_message: Boolean? = null,
//    val is_automatic_forward: Boolean? = null,
//    val reply_to_message: TgMessage? = null,
//    val via_bot: TgUser? = null,
//    val edit_date: TgDate? = null,
    val has_protected_content: Boolean? = null,
//    val is_from_offline: Boolean? = null,
//    val is_paid_post: Boolean? = null,
//    val media_group_id: String? = null,
//    val author_signature: String? = null,
//    val paid_star_count: Int? = null,
    val text: String? = null,
    val caption: String? = null,
)

fun TgMessage.toMessage(): Message =
    Message(message_id.mid, message_thread_id?.toLong().tpc!!, false)