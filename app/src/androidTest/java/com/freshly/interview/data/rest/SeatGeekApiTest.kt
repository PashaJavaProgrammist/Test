package com.freshly.interview.data.rest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.freshly.interview.common.Result
import com.freshly.interview.common.awaitResult
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Call

@RunWith(AndroidJUnit4::class)
class SeatGeekApiTest {

    lateinit var seatGeekApiService: SeatGeekApiService

    private val events = Events(
        events = listOf(
            Event(
                id = 123,
                datetimeUtc = "test_datetimeUtc",
                venue = Venue(
                    name = "test_name",
                    url = "test_url",
                ),
            )
        )
    )

    @Test
    fun seat_geek_api_test() {

        val mockCall = mock<Call<Events?>> {
            onBlocking { awaitResult() }.doReturn(Result.Success(events))
        }
        val mockSeatGeekApi = mock<SeatGeekApi> {
            on { events(anyString()) }.doReturn(mockCall)
        }
        seatGeekApiService = SeatGeekApiService(seatGeekApi = mockSeatGeekApi)

        val response: Result<Events?> = runBlocking {
            seatGeekApiService.events()
        }

        Assert.assertTrue(response is Result.Success)
        Assert.assertEquals((response as Result.Success).value, events)
    }
}