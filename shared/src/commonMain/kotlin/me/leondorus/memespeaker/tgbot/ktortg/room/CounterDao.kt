package me.leondorus.memespeaker.tgbot.ktortg.room

import androidx.room.*

@Dao
interface CounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCounter(counter: Counter)

    @Query("SELECT * FROM counters WHERE id = :counterId")
    suspend fun getCounter(counterId: String): Counter?

    @Transaction
    suspend fun maxCounter(counter: Counter) {
        val cur = getCounter(counter.id)?.value ?: 0
        if (counter.value > cur) {
            updateCounter(counter)
        }
    }

    @Delete
    suspend fun deleteCounter(counter: Counter)
}