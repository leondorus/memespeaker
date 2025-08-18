package me.leondorus.room.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: RoomMessage)

    @Update
    suspend fun update(message: RoomMessage)

    @Transaction
    suspend fun promote(id: Int): Boolean {
        val mess = findById(id).first() ?: return false
        if (mess.wasPromoted)
            return false

        update(mess.copy(wasPromoted = true))
        return true
    }

    @Query("SELECT * FROM messages")
    fun getAll(): Flow<List<RoomMessage>>

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    fun findById(id: Int): Flow<RoomMessage?>
}