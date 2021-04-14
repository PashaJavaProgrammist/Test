package com.freshly.interview.common

sealed class Result<T> {
    class Success<T>(val value: T?) : Result<T>()
    class Error<T>(val throwable: Throwable? = null) : Result<T>()
}