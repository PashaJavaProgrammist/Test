package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.data.db.EventDao
import com.freshly.interview.domain.EventDomain.Companion.toEventDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Save events in local DB
 */
class SaveEventsLocallyUseCase(
    private val dao: EventDao,
) : UseCase<List<EventDomain>, Unit> {

    override suspend fun execute(input: List<EventDomain>): Result<Unit> =
        withContext(Dispatchers.IO) {
            dao.insertAll(input.map { it.toEventDbo() })
            return@withContext Result.Success(Unit)
        }
}