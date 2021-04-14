package com.freshly.interview.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * from events ORDER BY id")
    fun getEventsFlow(): Flow<List<EventDbo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(events: List<EventDbo>)

    @Update
    fun update(event: EventDbo)

    @Query("UPDATE events SET favorite = :favorite WHERE id = :id")
    fun updateById(id: Long, favorite: Boolean)
}