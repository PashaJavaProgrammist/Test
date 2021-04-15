package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.data.db.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Observe changes of events in local db
 */
open class GetEventsFlowLocallyUseCase(
    private val dao: EventDao,
) : UseCase<Unit, Flow<List<EventDomain>>> {

    override suspend fun execute(input: Unit): Result<Flow<List<EventDomain>>> =
        withContext(Dispatchers.IO) {
            return@withContext Result.Success(dao.getEventsFlow().map { l ->
                l.map {
                    EventDomain(
                        id = it.id,
                        name = it.name,
                        url = it.url,
                        date = it.date,
                        time = it.time,
                        favorite = it.favorite
                    )
                }
            })
        }
}