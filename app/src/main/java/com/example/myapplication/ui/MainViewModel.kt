package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val countDownFlow = flow {
        val startingValue = 10
        var currentValue = startingValue

        emit(startingValue)

        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    private val _sharedFlow = MutableSharedFlow<Int>(1)
    val sharedFlow = _sharedFlow.shareIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), replay = 1
    )

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    private val _countDownStateFlow = MutableStateFlow(0)
    val countDownStateFlow = _countDownStateFlow.asStateFlow()

    init {
//        viewModelScope.launch {
//            stateFlow.collect {
//                Log.d("TAG", "$it ")
//            }
//        }
//        viewModelScope.launch {
//            sharedFlow.collect {
//                Log.d("TAG", "SHARED FLOW: The received number is $it")
//            }
//        }
        viewModelScope.launch {
            countDown()
        }
        squareNumber(12)
    }

    private suspend fun countDown() {
        val startingValue = 20
        var currentValue = startingValue

        _countDownStateFlow.emit(startingValue)

        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            _countDownStateFlow.emit(currentValue)
        }
    }

    fun squareNumber(number: Int) {
        viewModelScope.launch {
            _sharedFlow.emit(number * number)
        }
    }

    fun incrementCounter() {
        _stateFlow.value += 1
    }

    suspend fun fetchData(): List<String> {
        // Simulate a network delay
        delay(2000)
        return listOf(
            "Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5",
            "Item 6",
            "Item 7",
            "Item 8",
            "Item 9",
            "Item 10",
            "Item 11",
            "Item 12",
            "Item 13",
            "Item 14",
            "Item 15",
        )
    }

}