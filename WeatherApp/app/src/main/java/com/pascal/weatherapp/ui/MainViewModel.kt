package com.pascal.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.model.WeatherDTO
import java.sql.Timestamp

class MainViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
): ViewModel() {

    private lateinit var weatherBundle: WeatherDTO

    private val _index = MutableLiveData<Int>()


    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}