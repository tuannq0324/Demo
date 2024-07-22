package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _listName = MutableStateFlow<List<String>>(emptyList())
    val listName = _listName.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _listName.value = listOf(
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

    fun getData(): List<String> {
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

    fun updateName(currentName: String) {
        viewModelScope.launch {
            val oldList = _listName.value.toMutableList()
            val index = oldList.indexOf(currentName)
            if (index != -1) {
                oldList[index] = "Updated Name"
                _listName.value = oldList
            }
        }
    }

//    fun addItem(position: Int? = null) {
//        viewModelScope.launch {
//            val oldList = _listName.value.toMutableList()
//            oldList.add(position ?: oldList.size, "Added Item")
//            _listName.value = oldList
//        }
//    }

}