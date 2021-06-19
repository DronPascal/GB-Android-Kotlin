package com.pascal.weatherapp.ui

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.data.model.City
import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.data.model.WeatherRequest
import com.pascal.weatherapp.data.model.testWeatherDTO
import com.pascal.weatherapp.data.remote.WeatherRemoteDataSource
import com.pascal.weatherapp.data.remote.WeatherRepository
import com.pascal.weatherapp.data.remote.WeatherRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep


class MainViewModel(
    val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
    val weatherDtoLiveData: MutableLiveData<WeatherDTO> = MutableLiveData(),

    private val weatherRepository: WeatherRepository =
        WeatherRepositoryImpl(WeatherRemoteDataSource())
) : ViewModel() {

    private val handlerThread = HandlerThread("thread")
    private var handler: Handler

    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    fun initiateWeatherRefresh() {
        appStateLiveData.value = AppState.Loading
        initiateServerWeatherRefresh()
    }

    fun initiateServerWeatherRefresh() {
        City.getDefaultCity().let {
            weatherRepository.getWeatherDetailsFromServer(WeatherRequest(it.lat, it.lon), callBack)
        }
    }

    fun initiateTestWeatherRefresh() {
        handler.post {
            sleep(1000)
            appStateLiveData.postValue(AppState.Success)
            weatherDtoLiveData.postValue(testWeatherDTO)
        }
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

    companion object {
        private const val SERVER_ERROR = "Ошибка сервера"
        private const val REQUEST_ERROR = "Ошибка. Проверьте подключение к интернету"
        private const val CORRUPTED_DATA = "Данные повреждены"
    }

    override fun onCleared() {
        super.onCleared()
        handlerThread.quitSafely()
    }

}