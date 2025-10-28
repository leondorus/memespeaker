package me.leondorus.memespeaker.tgbot.ktortg.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.leondorus.memespeaker.data.MessageId
import me.leondorus.memespeaker.data.React
import me.leondorus.memespeaker.data.UserId

@Serializable
@Suppress("PropertyName")
sealed class TgReactionType {
    @Serializable
    @SerialName("emoji")
    data class Emoji(
        val emoji: String
    ) : TgReactionType()

    @Serializable
    @SerialName("custom_emoji")
    data class CustomEmoji(
        val custom_emoji_id: String
    ) : TgReactionType()

    @Serializable
    @SerialName("paid")
    data object Paid : TgReactionType()
}

fun TgReactionType.toReact(messageId: MessageId, userId: UserId): React =
    when (this) {
        is TgReactionType.Emoji -> React(emoji, messageId, userId)
        is TgReactionType.CustomEmoji -> React("❤️", messageId, userId)
        TgReactionType.Paid -> React("❤️", messageId, userId)
    }