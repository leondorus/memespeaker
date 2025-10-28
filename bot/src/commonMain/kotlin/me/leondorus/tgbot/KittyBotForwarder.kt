package me.leondorus.tgbot

import me.leondorus.data.*
import me.leondorus.promote.Forwarder
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.chats.ChatId
import org.bezsahara.kittybot.telegram.utils.TResult

class KittyBotForwarder(private val bot: KittyBot) : Forwarder {
    override suspend fun forwardMessageToTopic(
        messageId: MessageId,
        topic: TOPIC
    ): Message {
        val res =
            bot.forwardMessage(ChatId(MEMES_CHAT_ID), ChatId(MEMES_CHAT_ID), messageId.id, messageThreadId = topic.id)

        when (res) {
            is TResult.Failure -> throw RuntimeException("Did not get the message")
            is TResult.Success -> {
                val mess = res.value
                val id = mess.messageId.mid
                val newTopic = mess.messageThreadId.tpc!!
                val newMess = Message(id, newTopic, false)
                return newMess
            }
        }
    }
}