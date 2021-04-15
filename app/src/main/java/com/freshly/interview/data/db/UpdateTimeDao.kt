package com.freshly.interview.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateTimeDao {

    @Query("SELECT * from update_time WHERE id = :id")
    fun getUpdateTimeById(id: Long = UpdateTimeDbo.UPDATE_ID): UpdateTimeDbo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimeOrUpdateIfExist(event: UpdateTimeDbo)
}