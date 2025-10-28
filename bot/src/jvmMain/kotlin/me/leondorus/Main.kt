package me.leondorus

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.leondorus.checkers.ThreeReactChecker
import me.leondorus.promote.Promoter
import me.leondorus.room.getRoomDatabase
import me.leondorus.room.message.RoomMessageRepo
import me.leondorus.room.react.RoomReactRepo
import me.leondorus.savers.MessageSaver
import me.leondorus.savers.ReactSaver
import me.leondorus.tgbot.KtorForwarder
import me.leondorus.tgbot.MEMES_CHAT_ID
import me.leondorus.tgbot.ktortg.KtorBot
import me.leondorus.tgbot.ktortg.UpdateRepo

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