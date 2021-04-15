package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.common.time.DateTimeUtcConverter
import com.freshly.interview.data.rest.ApiService
import com.freshly.interview.data.rest.Event.Companion.toEventDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Request events from remote storage
 */
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
                            it.toEventDomain(
                                dateConvert = dateUtcConverter::convert,
                                timeConvert = timeUtcConverter::convert,
                            )
                        } ?: emptyList()
                    )
                )
            }
            is Result.Error -> Result.Error(r.throwable)
        }
    }

    data class Output(
        val events: List<EventDomain>
    )
}

