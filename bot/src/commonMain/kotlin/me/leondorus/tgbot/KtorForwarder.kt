package me.leondorus.tgbot

import me.leondorus.data.Message
import me.leondorus.data.MessageId
import me.leondorus.data.TOPIC
import me.leondorus.promote.Forwarder
import me.leondorus.tgbot.ktortg.KtorBot
import me.leondorus.tgbot.ktortg.data.toMessage

class KtorForwarder(private val ktorBot: KtorBot, private val chatId: Long) : Forwarder {
    override suspend fun forwardMessageToTopic(
        messageId: MessageId,
        topic: TOPIC
    ): Message {
        val resMessage = ktorBot.forwardMessage(chatId, chatId, messageId.id, topic.id)
        return resMessage.toMessage()
    }
}