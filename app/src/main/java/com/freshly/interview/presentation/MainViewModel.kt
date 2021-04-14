package com.freshly.interview.presentation

import android.net.Uri
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freshly.interview.common.Result
import com.freshly.interview.domain.GetEventsFlowLocallyUseCase
import com.freshly.interview.domain.UpdateEventFavoriteByIdLocallyUseCase
import com.freshly.interview.domain.UpdateEventsUseCase
import com.freshly.interview.presentation.EventPresentation.Companion.toEventPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    private val updateEventsUseCase: UpdateEventsUseCase,
    private val getEventsFlowLocallyUseCase: GetEventsFlowLocallyUseCase,
    private val updateEventFavoriteByIdLocallyUseCase: UpdateEventFavoriteByIdLocallyUseCase,
) : ViewModel() {

    private val _eventsData = MutableLiveData<List<EventPresentation>>()
    val eventsData: LiveData<List<EventPresentation>> get() = _eventsData

    private val _errorData = MutableLiveData<String>() // todo: use SingleLiveEvent
    val errorData: LiveData<String> get() = _errorData

    private val _progressData = MutableLiveData<Boolean>()
    val progressData: LiveData<Boolean> get() = _progressData

    private val _browserData = MutableLiveData<Uri>() // todo: use SingleLiveEvent
    val browserData: LiveData<Uri> get() = _browserData

    private var showAllEventsFlow = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            _progressData.value = true
            updateEventsUseCase.execute(Unit)
            when (val r = getEventsFlowLocallyUseCase.execute(Unit)) {
                is Result.Success -> {
                    _progressData.value = false
                    r.value
                        ?.combine(showAllEventsFlow) { list, showAll -> list to showAll }
                        ?.collect { listToShowAll ->
                            _eventsData.value = listToShowAll.first
                                .map { it.toEventPresentation() }
                                .filter { listToShowAll.second || it.favorite }
                        }
                }
                is Result.Error -> {
                    _progressData.value = false
                    _errorData.value = r.throwable?.message ?: "Oops..." // todo: in strings.xml
                }
            }
        }
    }

    fun showAllEvents(showAll: Boolean) {
        showAllEventsFlow.value = showAll
    }

    fun openUrl(url: String) {
        if (PatternsCompat.WEB_URL
                .matcher(url)
                .matches()
        ) { // todo: extract validation in separate class
            _browserData.value = Uri.parse(url)
        }
    }

    fun makeFavoriteOrNot(id: Long, fav: Boolean) {
        viewModelScope.launch {
            updateEventFavoriteByIdLocallyUseCase.execute(
                UpdateEventFavoriteByIdLocallyUseCase.Input(
                    id = id,
                    fav = fav,
                )
            )
        }
    }
}