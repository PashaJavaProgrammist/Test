package com.freshly.interview.common.time

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TimeConverterTest {

    private val timeConverter = TimeConverter()
    private val dateConverter = DateConverter()

    @Test
    fun time_converter_test() {
        listOf(
            "10:30:00" to "2021-04-14T10:30:00",
            "03:30:00" to "2021-04-14T03:30:00"
        ).map {
            assertEquals(it.first, timeConverter.convert(it.second))
        }
    }

    @Test
    fun date_converter_test() {
        listOf(
            "2021-04-14" to "2021-04-14T10:30:00",
            "2021-04-14" to "2021-04-14T03:30:00"
        ).map {
            assertEquals(it.first, dateConverter.convert(it.second))
        }
    }
}