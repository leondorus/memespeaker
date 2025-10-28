package me.leondorus.tgbot.ktortg.data

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class TgMessageReactionUpdated(
    val chat: TgChat,
    val message_id: Long,
    val user: TgUser? = null,
    val actor_chat: TgChat? = null,
    val date: TgDate,
    val old_reaction: List<TgReactionType>,
    val new_reaction: List<TgReactionType>,
)
