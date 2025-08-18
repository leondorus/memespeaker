package me.leondorus.room.react

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReactDao {
    @Insert
    suspend fun insert(react: RoomReact): Long

    @Delete
    suspend fun delete(react: RoomReact)

    @Query("SELECT * FROM reacts WHERE messageId = :mid")
    fun getByMessageId(mid: Int): Flow<List<RoomReact>>
}