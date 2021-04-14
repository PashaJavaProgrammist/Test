package com.freshly.interview.di

import com.freshly.interview.common.time.DateConverter
import com.freshly.interview.common.time.DateTimeUtcConverter
import com.freshly.interview.common.time.TimeConverter
import com.freshly.interview.data.rest.ApiService
import com.freshly.interview.data.rest.SeatGeekApi
import com.freshly.interview.data.rest.SeatGeekApiService
import com.freshly.interview.domain.GetEventsUseCase
import com.freshly.interview.presentation.MainViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.seatgeek.com/2/"

val appModules = module {
    viewModel { MainViewModel(getEventsUseCase = get()) }

    factory {
        GetEventsUseCase(
            apiService = get(),
            timeUtcConverter = get(named("Time")),
            dateUtcConverter = get(named("Date")),
        )
    }
    factory<DateTimeUtcConverter>(named("Time")) { TimeConverter() }
    factory<DateTimeUtcConverter>(named("Date")) { DateConverter() }
    factory<ApiService> { SeatGeekApiService(seatGeekApi = get()) }

    single<SeatGeekApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SeatGeekApi::class.java)
    }
}