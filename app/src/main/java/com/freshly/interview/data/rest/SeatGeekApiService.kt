package com.freshly.interview.data.rest

import com.freshly.interview.common.Result
import com.freshly.interview.common.awaitResult

private const val CLIENT_ID = "MjE3MTU3MjV8MTYxODQxMDMzNS44NzY4MDQ4" // todo: Move in secure place

open class SeatGeekApiService(private val seatGeekApi: SeatGeekApi) : ApiService {

    override suspend fun events(): Result<Events?> {
        return seatGeekApi.events(clientId = CLIENT_ID).awaitResult()
    }
}