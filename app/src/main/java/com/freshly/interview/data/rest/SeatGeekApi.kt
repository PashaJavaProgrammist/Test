package com.freshly.interview.data.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeatGeekApi {

    @GET("events")
    fun events(@Query("client_id") clientId: String): Call<Events?>
}