package me.leondorus.memespeaker

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.leondorus.memespeaker.checkers.ThreeReactChecker
import me.leondorus.memespeaker.promote.Promoter
import me.leondorus.memespeaker.room.getRoomDatabase
import me.leondorus.memespeaker.room.message.RoomMessageRepo
import me.leondorus.memespeaker.room.react.RoomReactRepo
import me.leondorus.memespeaker.savers.MessageSaver
import me.leondorus.memespeaker.savers.ReactSaver
import me.leondorus.memespeaker.tgbot.KtorForwarder
import me.leondorus.memespeaker.tgbot.MEMES_CHAT_ID
import me.leondorus.memespeaker.tgbot.ktortg.KtorBot
import me.leondorus.memespeaker.tgbot.ktortg.UpdateRepo

fun main(): Unit = runBlocking {
    val token = System.getenv("BOT_TOKEN")
    val bot = KtorBot(token, this)

    val database = getDatabaseBuilder().getRoomDatabase()
    val messRepo = RoomMessageRepo(database.messageDao())
    val reactRepo = RoomReactRepo(database.reactDao())

    val updateRepo = UpdateRepo(MEMES_CHAT_ID, bot, this)
    val reactSaver = ReactSaver(updateRepo, reactRepo)
    val messageRepo = MessageSaver(updateRepo, messRepo)

    val forwarder = KtorForwarder(bot, MEMES_CHAT_ID)
    val checker = ThreeReactChecker()

    val promoter = Promoter(reactRepo, messRepo, checker, forwarder)
    launch {
        reactSaver.startSaving()
    }
    launch {
        messageRepo.startSaving()
    }
    launch {
        promoter.startCollectingFlow()
    }
}