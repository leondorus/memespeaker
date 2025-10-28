package me.leondorus.memespeaker.tgbot

import me.leondorus.memespeaker.data.Message
import me.leondorus.memespeaker.data.MessageId
import me.leondorus.memespeaker.data.TOPIC
import me.leondorus.memespeaker.promote.Forwarder
import me.leondorus.memespeaker.tgbot.ktortg.KtorBot
import me.leondorus.memespeaker.tgbot.ktortg.data.toMessage

class KtorForwarder(private val ktorBot: KtorBot, private val chatId: Long) : Forwarder {
    override suspend fun forwardMessageToTopic(
        messageId: MessageId,
        topic: TOPIC
    ): Message {
        val resMessage = ktorBot.forwardMessage(chatId, chatId, messageId.id, topic.id)
        return resMessage.toMessage()
    }
}