package com.pascal.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.data.model.WeatherRequest
import com.pascal.weatherapp.data.remote.WeatherRemoteDataSource
import com.pascal.weatherapp.data.remote.WeatherRepository
import com.pascal.weatherapp.data.remote.WeatherRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(
    val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
    val weatherDtoLiveData: MutableLiveData<WeatherDTO> = MutableLiveData(),

    private val weatherRepository: WeatherRepository =
        WeatherRepositoryImpl(WeatherRemoteDataSource())
) : ViewModel() {

    fun getWeatherFromRemoteSource(requestDto: WeatherRequest) {
        appStateLiveData.value = AppState.Loading
        weatherRepository.getWeatherDetailsFromServer(requestDto, callBack)
    }

    private val callBack = object : Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                checkResponse(serverResponse)
            } else {
                appStateLiveData.postValue(AppState.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            appStateLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO) {
            with(serverResponse) {
                return if (listOf(info, fact, forecast, now, now_dt, info?.url)
                        .any { it == null }
                ) {
                    appStateLiveData.postValue(AppState.Error(Throwable(CORRUPTED_DATA)))
                } else {
                    weatherDtoLiveData.postValue(serverResponse)
                }
            }
        }

    }

    companion object {
        private const val SERVER_ERROR = "Ошибка сервера"
        private const val REQUEST_ERROR = "Ошибка. Проверьте подключение к интернету"
        private const val CORRUPTED_DATA = "Данные повреждены"
    }
}