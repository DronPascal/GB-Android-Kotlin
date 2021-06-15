package com.pascal.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pascal.weatherapp.app.AppState
import java.sql.Timestamp

class HomeViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
): ViewModel() {

    private val _index = MutableLiveData<Int>()
    private val answerTimestampLD = MutableLiveData<Timestamp>()
    private val tomorrowTimestampLD = MutableLiveData<Timestamp>()


    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}