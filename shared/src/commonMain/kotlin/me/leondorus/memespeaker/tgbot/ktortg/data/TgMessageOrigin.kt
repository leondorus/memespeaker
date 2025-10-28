@file:Suppress("unused")

package me.leondorus.memespeaker.tgbot.ktortg.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
sealed class TgMessageOrigin {
    abstract val date: TgDate

    @Serializable
    @SerialName("user")
    data class User(
        override val date: TgDate,

        val sender_user: TgUser,
    ) : TgMessageOrigin()

    @Serializable
    @SerialName("hidden_user")
    data class HiddenUser(
        override val date: TgDate,

        val sender_user_name: String,
    ) : TgMessageOrigin()

    @Serializable
    @SerialName("chat")
    data class Chat(
        override val date: TgDate,

        val sender_chat: TgChat,
        val author_signature: String? = null,
    ) : TgMessageOrigin()

    @Serializable
    @SerialName("channel")
    data class Channel(
        override val date: TgDate,

        val chat: TgChat,
        val message_id: Long,
        val author_signature: String? = null,
    ) : TgMessageOrigin()
}