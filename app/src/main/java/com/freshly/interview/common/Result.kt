package com.freshly.interview.common

/**
 * Wraps result of network operations or usecases result
 */
sealed class Result<T> {

    class Success<T>(val value: T?) : Result<T>()
    class Error<T>(val throwable: Throwable? = null) : Result<T>()
}