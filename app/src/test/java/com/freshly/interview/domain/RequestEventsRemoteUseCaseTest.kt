package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.common.time.DateConverter
import com.freshly.interview.common.time.TimeConverter
import com.freshly.interview.data.rest.Event
import com.freshly.interview.data.rest.Event.Companion.toEventDomain
import com.freshly.interview.data.rest.Events
import com.freshly.interview.data.rest.SeatGeekApiService
import com.freshly.interview.data.rest.Venue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RequestEventsRemoteUseCaseTest {

    private val timeConverter = TimeConverter()
    private val dateConverter = DateConverter()

    private val events = Events(
        events = listOf(
            Event(
                id = 123,
                datetimeUtc = "2021-04-14T03:30:00",
                venue = Venue(
                    name = "test_name",
                    url = "test_url",
                ),
            )
        )
    )

    @Test
    fun request_events_remote_use_case_test() {
        val mockApiService = mock<SeatGeekApiService> {
            onBlocking { events() }.doReturn(Result.Success(events))
        }
        val requestEventsRemoteUseCase =
            RequestEventsRemoteUseCase(mockApiService, timeConverter, dateConverter)
        val events: List<EventDomain> =
            (runBlocking { requestEventsRemoteUseCase.execute(Unit) } as? Result.Success)?.value?.events
                ?: emptyList()
        assertEquals(
            events.first(),
            this.events.events
                ?.first()
                ?.toEventDomain(
                    timeConvert = timeConverter::convert,
                    dateConvert = dateConverter::convert,
                )
        )
    }
}