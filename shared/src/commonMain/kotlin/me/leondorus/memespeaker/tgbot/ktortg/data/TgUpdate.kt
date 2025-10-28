package me.leondorus.memespeaker.tgbot.ktortg.data

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class TgUpdate(
    val update_id: Int,
    val message: TgMessage? = null,
//    val edited_message: TgMessage?,
    val message_reaction: TgMessageReactionUpdated? = null,
//    val message_reaction_count: TgMessageReactionCountUpdated?,
)