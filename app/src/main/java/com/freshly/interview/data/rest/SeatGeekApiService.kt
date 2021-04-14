package com.freshly.interview.data.rest

import com.freshly.interview.common.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val CLIENT_ID = "MjE3MTU3MjV8MTYxODQxMDMzNS44NzY4MDQ4" // todo: Move in secure place

class SeatGeekApiService(private val seatGeekApi: SeatGeekApi) : ApiService {

    override suspend fun events(): Result<Events> = suspendCoroutine { cont ->
        seatGeekApi.events(clientId = CLIENT_ID).enqueue(object : Callback<Events?> {
            override fun onResponse(call: Call<Events?>, response: Response<Events?>) {
                cont.resume(
                    if (response.isSuccessful) {
                        Result.Success(value = response.body())
                    } else {
                        Result.Error()
                    }
                )
            }

            override fun onFailure(call: Call<Events?>, t: Throwable) {
                cont.resume(Result.Error(throwable = t))
            }
        })
    }
}