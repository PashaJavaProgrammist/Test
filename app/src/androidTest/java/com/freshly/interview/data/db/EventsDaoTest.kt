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
class EventsDaoTest {

    private lateinit var eventDao: EventDao
    private lateinit var db: AppDatabase

    private val testEvent = EventDbo(
        id = 123,
        name = "test_name",
        url = "test_url",
        time = "test_time",
        date = "test_date",
        favorite = false,
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        eventDao = db.eventDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun write_and_read_events_test() {
        val insertList = listOf(testEvent)
        eventDao.insertAll(insertList)
        val events = eventDao.getEvents()
        assertEquals(events.size, 1)
        assertEquals(insertList.first(), events.first())
    }

    @Test
    fun update_by_id_event_test() {
        val insertList = listOf(testEvent)
        eventDao.insertAll(insertList)
        eventDao.updateById(testEvent.id, true)
        val events = eventDao.getEvents()
        assertTrue(events.first().favorite)
    }

    @Test
    fun update_event_test() {
        val insertList = listOf(testEvent)
        eventDao.insertAll(insertList)
        eventDao.update(testEvent.copy(favorite = true))
        val events = eventDao.getEvents()
        assertTrue(events.first().favorite)
    }
}