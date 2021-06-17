package com.pascal.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.data.model.*
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

    fun initiateWeatherRefresh() {
        appStateLiveData.value = AppState.Loading
        initiateTestWeatherRefresh()
    }

    private val callBack = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                validateResponse(serverResponse)
            } else {
                appStateLiveData.postValue(AppState.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            appStateLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun validateResponse(response: WeatherDTO) {
            with(response) {
                return if (listOf(info, fact, forecast, now, now_dt, info?.url)
                        .any { it == null }
                ) {
                    appStateLiveData.postValue(AppState.Error(Throwable(CORRUPTED_DATA)))
                } else {
                    appStateLiveData.postValue(AppState.Success)
                    weatherDtoLiveData.postValue(response)
                }
            }
        }
    }

    fun initiateServerWeatherRefresh(){
        City.getDefaultCity().let {
            weatherRepository.getWeatherDetailsFromServer(WeatherRequest(it.lat, it.lon), callBack)
        }
    }

    fun initiateTestWeatherRefresh() {
        appStateLiveData.postValue(AppState.Success)
        weatherDtoLiveData.postValue(
            WeatherDTO(
                now = 1623938412,
                now_dt = "2021-06-17T14:00:12.130872Z",
                info = InfoDTO(
                    lat = 55.755826,
                    lon = 37.617299900000035,
                    url = "https://yandex.ru/pogoda/213?lat=55.755826\u0026lon=37.6172999"
                ),
                fact = FactDTO(
                    temp = (-20..30).random(),
                    feels_like = (-20..30).random(),
                    icon = "skc_d",
                    condition = "clear"
                ),
                forecast = ForecastDTO(
                    parts = listOf(
                        PartsDTO(
                            temp_min = (-20..30).random(),
                            temp_max = (-20..30).random(),
                            prec_prob = (0..99).random()
                        )
                    )
                )
            )
        )
    }

    companion object {
        private const val SERVER_ERROR = "Ошибка сервера"
        private const val REQUEST_ERROR = "Ошибка. Проверьте подключение к интернету"
        private const val CORRUPTED_DATA = "Данные повреждены"
    }
}