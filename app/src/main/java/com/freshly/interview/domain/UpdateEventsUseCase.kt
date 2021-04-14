package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.data.db.EventDao
import com.freshly.interview.data.db.UpdateTimeDao
import com.freshly.interview.data.db.UpdateTimeDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val UPDATE_INTERVAL_MS = 3_600_000L

class UpdateEventsUseCase(
    private val requestEventsRemoteUseCase: RequestEventsRemoteUseCase,
    private val saveEventsLocallyUseCase: SaveEventsLocallyUseCase,
    private val updateTimeDao: UpdateTimeDao,
    private val eventDao: EventDao,
) : UseCase<UpdateEventsUseCase.Input, Unit> {

    override suspend fun execute(input: Input): Result<Unit> = withContext(Dispatchers.IO) {
        val timeDbo = updateTimeDao.getById()
        val lastUpdateTime = timeDbo?.time ?: 0
        if (input.forceUpdate || System.currentTimeMillis() - lastUpdateTime > UPDATE_INTERVAL_MS) {
            when (val result = requestEventsRemoteUseCase.execute(Unit)) {
                is Result.Success -> {
                    val localEvents = eventDao.getEvents()
                    result.value?.events?.let { remoteEvents ->
                        saveEventsLocallyUseCase.execute(
                            remoteEvents.map { re ->
                                re.favorite = localEvents.find { it.id == re.id }
                                    ?.favorite ?: false
                                re
                            }
                        )
                    }
                    updateTimeDao.updateTime(UpdateTimeDbo(time = System.currentTimeMillis()))
                }
                is Result.Error -> return@withContext Result.Error(result.throwable)
            }
        }
        return@withContext Result.Success(Unit)
    }

    data class Input(
        val forceUpdate: Boolean
    )
}