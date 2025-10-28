package me.leondorus.tgbot.ktortg.data

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class TgUser(
    val id: Long,
    val is_bot: Boolean,
    val first_name: String,
    val last_name: String? = null,
    val username: String? = null,
    val language_code: String? = null,
    val is_premium: Boolean? = null,
    val added_to_attachment_menu: Boolean? = null,
    val can_join_groups: Boolean? = null,
    val can_read_all_group_messages: Boolean? = null,
    val supports_inline_queries: Boolean? = null,
    val can_connect_to_business: Boolean? = null,
    val has_main_web_app: Boolean? = null,
)