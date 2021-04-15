package com.freshly.interview.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.freshly.interview.common.Result
import com.freshly.interview.domain.EventDomain
import com.freshly.interview.domain.GetEventsFlowLocallyUseCase
import com.freshly.interview.domain.UpdateEventFavoriteByIdLocallyUseCase
import com.freshly.interview.domain.UpdateEventsUseCase
import com.freshly.interview.presentation.EventPresentation.Companion.toEventPresentation
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val testEventDomain = EventDomain(
        id = 12312,
        name = "test_id",
        url = "https://google.com/",
        date = "test_date",
        time = "test_time",
        favorite = true
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun view_model_get_events_data_test() {
        val mockUpdateEventsUseCase = mock<UpdateEventsUseCase> {
            onBlocking { execute(any()) }.thenReturn(Result.Success(Unit))
        }
        val mockGetEventsFlowLocallyUseCase = mock<GetEventsFlowLocallyUseCase> {
            onBlocking { execute(any()) }.thenReturn(Result.Success(flowOf(listOf(testEventDomain))))
        }
        val mockUpdateEventFavoriteByIdLocallyUseCase =
            mock<UpdateEventFavoriteByIdLocallyUseCase> {
                onBlocking { execute(any()) }.thenReturn(Result.Success(Unit))
            }
        val mainViewModel = MainViewModel(
            updateEventsUseCase = mockUpdateEventsUseCase,
            getEventsFlowLocallyUseCase = mockGetEventsFlowLocallyUseCase,
            updateEventFavoriteByIdLocallyUseCase = mockUpdateEventFavoriteByIdLocallyUseCase,
        )
        val flow = MutableStateFlow<List<EventPresentation>>(listOf())
        mainViewModel.eventsData.observeForever {
            flow.value = it
        }
        runBlocking {
            assertEquals(
                testEventDomain.toEventPresentation(),
                flow.first { it.isNotEmpty() }.first()
            )
        }
    }
}