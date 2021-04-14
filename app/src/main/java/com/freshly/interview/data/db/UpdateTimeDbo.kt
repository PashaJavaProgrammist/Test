package com.freshly.interview.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "update_time")
data class UpdateTimeDbo(
    @ColumnInfo(name = "time")
    val time: Long,
) {

    @PrimaryKey(autoGenerate = false)
    var id: Long = UPDATE_ID

    companion object {

        const val UPDATE_ID = 1314L
    }
}