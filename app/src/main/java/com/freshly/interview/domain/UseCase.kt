package com.freshly.interview.domain

import com.freshly.interview.common.Result

/**
 * Common interface for usecases on domain layer
 *
 * @see Result
 */
interface UseCase<Input, Output> {

    suspend fun execute(input: Input): Result<Output>
}