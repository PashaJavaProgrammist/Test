package com.freshly.interview.domain

import com.freshly.interview.common.Result
import com.freshly.interview.data.db.UpdateTimeDao
import com.freshly.interview.data.db.UpdateTimeDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val UPDATE_INTERVAL_MS = 3_600_000L

class UpdateEventsUseCase(
    private val requestEventsRemoteUseCase: RequestEventsRemoteUseCase,
    private val saveEventsLocallyUseCase: SaveEventsLocallyUseCase,
    private val updateTimeDao: UpdateTimeDao,
) : UseCase<Unit, Unit> {

    override suspend fun execute(input: Unit): Result<Unit> = withContext(Dispatchers.IO) {
        val timeDbo = updateTimeDao.getById()
        val lastUpdateTime = timeDbo?.time ?: 0
        if (System.currentTimeMillis() - lastUpdateTime > UPDATE_INTERVAL_MS) {
            when (val result = requestEventsRemoteUseCase.execute(Unit)) {
                is Result.Success -> {
                    result.value?.events?.let { saveEventsLocallyUseCase.execute(it) }
                    updateTimeDao.updateTime(UpdateTimeDbo(time = System.currentTimeMillis()))
                }
                is Result.Error -> return@withContext Result.Error(result.throwable)
            }
        }
        return@withContext Result.Success(Unit)
    }
}