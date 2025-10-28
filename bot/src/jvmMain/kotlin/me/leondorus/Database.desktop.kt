package me.leondorus

import androidx.room.Room
import androidx.room.RoomDatabase
import me.leondorus.room.AppDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
//    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    val dbFile = File("./.actual-bot-dbs/my-room.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}