package com.freshly.interview.domain

import com.freshly.interview.common.Result

interface UseCase<Input, Output> {

    suspend fun execute(input: Input): Result<Output>
}