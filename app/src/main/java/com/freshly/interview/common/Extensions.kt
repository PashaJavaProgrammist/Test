package com.freshly.interview.common

import retrofit2.Call
import retrofit2.awaitResponse

val String.Companion.EMPTY get() = ""

suspend fun <T> Call<T>.awaitResult(): Result<T> {
    return try {
        val response = this.awaitResponse()
        if (response.isSuccessful) {
            Result.Success(value = response.body())
        } else {
            Result.Error()
        }
    } catch (t: Throwable) {
        Result.Error(throwable = t)
    }
}