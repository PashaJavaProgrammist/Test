package com.freshly.interview.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freshly.interview.common.Result
import com.freshly.interview.domain.GetEventsUseCase
import com.freshly.interview.presentation.EventPresentation.Companion.toEventPresentation
import kotlinx.coroutines.launch

class MainViewModel(
    private val getEventsUseCase: GetEventsUseCase,
) : ViewModel() {

    private val _eventsData = MutableLiveData<List<EventPresentation>>()
    val eventsData: LiveData<List<EventPresentation>> get() = _eventsData

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = _errorData

    init {
        viewModelScope.launch {
            when (val o = getEventsUseCase.execute(Unit)) {
                is Result.Success -> {
                    _eventsData.value = o.value?.events?.map {
                        it.toEventPresentation()
                    } ?: emptyList()
                }
                is Result.Error -> {
                    _errorData.value = o.throwable?.message ?: "Oops..." // todo: in strings.xml
                }
            }
        }
    }
}