package me.leondorus.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import me.leondorus.room.message.MessageDao
import me.leondorus.room.react.ReactDao
import me.leondorus.room.message.RoomMessage
import me.leondorus.room.react.RoomReact

@Database(entities = [RoomMessage::class, RoomReact::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun reactDao(): ReactDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

fun RoomDatabase.Builder<AppDatabase>.getRoomDatabase(): AppDatabase {
    return this
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}