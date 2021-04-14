package com.freshly.interview.domain

import com.freshly.interview.common.EMPTY
import com.freshly.interview.common.Result
import com.freshly.interview.common.time.DateTimeUtcConverter
import com.freshly.interview.data.rest.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestEventsRemoteUseCase(
    private val apiService: ApiService,
    private val timeUtcConverter: DateTimeUtcConverter,
    private val dateUtcConverter: DateTimeUtcConverter,
) : UseCase<Unit, RequestEventsRemoteUseCase.Output> {

    override suspend fun execute(input: Unit): Result<Output> {
        return when (val r = withContext(Dispatchers.IO) { apiService.events() }) {
            is Result.Success -> {
                Result.Success(
                    value = Output(
                        events = r.value?.events?.map {
                            EventDomain(
                                id = it.id ?: 0L,
                                name = it.venue?.name ?: String.EMPTY,
                                url = it.venue?.url ?: String.EMPTY,
                                date = it.datetimeUtc?.let { it1 -> date(it1) } ?: String.EMPTY,
                                time = it.datetimeUtc?.let { it1 -> time(it1) } ?: String.EMPTY,
                                favorite = false
                            )
                        } ?: emptyList()
                    )
                )
            }
            is Result.Error -> Result.Error(r.throwable)
        }
    }

    private fun date(dateTimeUtc: String): String {
        return dateUtcConverter.convert(dateTimeUtc)
    }

    private fun time(dateTimeUtc: String): String {
        return timeUtcConverter.convert(dateTimeUtc)
    }

    data class Output(
        val events: List<EventDomain>
    )
}

