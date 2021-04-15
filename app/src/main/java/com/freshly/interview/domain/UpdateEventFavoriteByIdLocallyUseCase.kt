package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.data.db.EventDao
import com.freshly.interview.domain.UpdateEventFavoriteByIdLocallyUseCase.Input
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Update event in local DB. Change [EventDomain.favorite] flag
 *
 * @see Input
 */
open class UpdateEventFavoriteByIdLocallyUseCase(
    private val dao: EventDao,
) : UseCase<UpdateEventFavoriteByIdLocallyUseCase.Input, Unit> {

    override suspend fun execute(input: Input): Result<Unit> =
        withContext(Dispatchers.IO) {
            dao.updateById(input.id, input.fav)
            return@withContext Result.Success(Unit)
        }

    data class Input(
        val id: Long,
        val fav: Boolean,
    )
}