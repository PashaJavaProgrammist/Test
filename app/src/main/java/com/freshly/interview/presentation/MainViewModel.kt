package com.freshly.interview.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freshly.interview.common.Result
import com.freshly.interview.domain.GetEventsFlowLocallyUseCase
import com.freshly.interview.domain.UpdateEventsUseCase
import com.freshly.interview.presentation.EventPresentation.Companion.toEventPresentation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val updateEventsUseCase: UpdateEventsUseCase,
    private val getEventsFlowLocallyUseCase: GetEventsFlowLocallyUseCase,
) : ViewModel() {

    private val _eventsData = MutableLiveData<List<EventPresentation>>()
    val eventsData: LiveData<List<EventPresentation>> get() = _eventsData

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = _errorData

    private val _progressData = MutableLiveData<Boolean>()
    val progressData: LiveData<Boolean> get() = _progressData

    init {
        viewModelScope.launch {
            _progressData.value = true
            updateEventsUseCase.execute(Unit)
            when (val r = getEventsFlowLocallyUseCase.execute(Unit)) {
                is Result.Success -> {
                    _progressData.value = false
                    r.value?.collect { l ->
                        _eventsData.value = l.map { it.toEventPresentation() }
                    }
                }
                is Result.Error -> {
                    _progressData.value = false
                    _errorData.value = r.throwable?.message ?: "Oops..." // todo: in strings.xml
                }
            }
        }
    }
}