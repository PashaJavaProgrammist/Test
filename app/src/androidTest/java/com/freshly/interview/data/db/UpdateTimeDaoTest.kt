package com.freshly.interview.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class UpdateTimeDaoTest {

    private lateinit var updateTimeDao: UpdateTimeDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        updateTimeDao = db.updateTimeDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_time_or_update_if_exist_test() {
        updateTimeDao.insertTimeOrUpdateIfExist(UpdateTimeDbo(time = 0))
        val time = updateTimeDao.getUpdateTimeById()?.time ?: -1
        assertEquals(time, 0)
        updateTimeDao.insertTimeOrUpdateIfExist(UpdateTimeDbo(time = 10))
        val timeUpdate = updateTimeDao.getUpdateTimeById()?.time ?: -1
        assertEquals(timeUpdate, 10)
    }
}