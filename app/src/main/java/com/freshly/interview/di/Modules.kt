package com.freshly.interview.di

import androidx.room.Room
import com.freshly.interview.common.time.DateConverter
import com.freshly.interview.common.time.DateTimeUtcConverter
import com.freshly.interview.common.time.TimeConverter
import com.freshly.interview.data.db.AppDatabase
import com.freshly.interview.data.rest.ApiService
import com.freshly.interview.data.rest.SeatGeekApi
import com.freshly.interview.data.rest.SeatGeekApiService
import com.freshly.interview.domain.GetEventsFlowLocallyUseCase
import com.freshly.interview.domain.RequestEventsRemoteUseCase
import com.freshly.interview.domain.SaveEventsLocallyUseCase
import com.freshly.interview.domain.UpdateEventFavoriteByIdLocallyUseCase
import com.freshly.interview.domain.UpdateEventsUseCase
import com.freshly.interview.presentation.MainViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.seatgeek.com/2/"
private const val DATABASE_NAME = "database"

val appModules = module {
    viewModel {
        MainViewModel(
            getEventsFlowLocallyUseCase = get(),
            updateEventsUseCase = get(),
            updateEventFavoriteByIdLocallyUseCase = get()
        )
    }

    factory {
        RequestEventsRemoteUseCase(
            apiService = get(),
            timeUtcConverter = get(named("Time")),
            dateUtcConverter = get(named("Date")),
        )
    }
    factory { SaveEventsLocallyUseCase(dao = get()) }
    factory { GetEventsFlowLocallyUseCase(dao = get()) }
    factory { UpdateEventFavoriteByIdLocallyUseCase(dao = get()) }
    factory {
        UpdateEventsUseCase(
            requestEventsRemoteUseCase = get(),
            saveEventsLocallyUseCase = get(),
            updateTimeDao = get(),
            eventDao = get(),
        )
    }

    factory<DateTimeUtcConverter>(named("Time")) { TimeConverter() }
    factory<DateTimeUtcConverter>(named("Date")) { DateConverter() }
    factory<ApiService> { SeatGeekApiService(seatGeekApi = get()) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<SeatGeekApi> {
        val r: Retrofit = get()
        r.create(SeatGeekApi::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single {
        val d: AppDatabase = get()
        d.eventDao()
    }

    single {
        val d: AppDatabase = get()
        d.updateTimeDao()
    }
}