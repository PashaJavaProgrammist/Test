package com.freshly.interview.data.rest

import com.freshly.interview.common.Result

interface ApiService {

    suspend fun events(): Result<Events>
}