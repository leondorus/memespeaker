package me.leondorus.tgbot.ktortg.data

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class TgChat(
    val id: Long,
    val type: String,
    val title: String? = null,
    val username: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val is_forum: Boolean? = null,
    val is_direct_messages: Boolean? = null,
)