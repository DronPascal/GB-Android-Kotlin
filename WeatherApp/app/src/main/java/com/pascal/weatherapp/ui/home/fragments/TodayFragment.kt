package com.pascal.weatherapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pascal.weatherapp.R
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.data.model.City
import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.data.model.WeatherRequest
import com.pascal.weatherapp.databinding.HomeFragmentTodayBinding
import com.pascal.weatherapp.ui.home.HomeActivity
import com.pascal.weatherapp.utils.resFromCondition
import com.pascal.weatherapp.utils.showSnackBar
import java.text.SimpleDateFormat
import java.util.*


class TodayFragment : Fragment() {

    private var _binding: HomeFragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel get() = (requireActivity() as HomeActivity).mainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initRefresher()

        viewModel.weatherDtoLiveData.observe(viewLifecycleOwner, {
            displayNewWeather(it)
        })

        viewModel.appStateLiveData.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = false
            when (it) {
                is AppState.Success -> {
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
                is AppState.Loading -> {
                    binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    it.error.message?.let { msg -> binding.root.showSnackBar(msg) }
                }
            }
        })
    }

    private fun initRefresher() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.initiateWeatherRefresh()
            binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
        }
    }

    private fun displayNewWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            includedLoadingLayout.loadingLayout.visibility = View.GONE

            // update date
            weatherDTO.now?.let {
                val date = Date().apply { time = System.currentTimeMillis() }
                val dateFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
                val dateLocaleStr = dateFormat.format(date)
                textviewTime.text = dateLocaleStr
            }
            // max, min temp
            weatherDTO.forecast?.parts?.get(0)?.let {
                if (it.temp_max != null && it.temp_min != null) {
                    textviewDayNightTemp.text =
                        getString(R.string.template_day_night).format(it.temp_max, it.temp_min)
                }
            }
            // current temp
            weatherDTO.fact?.temp?.let {
                textviewTemp.text = it.toString()
            }
            // feels like
            weatherDTO.fact?.feels_like?.let {
                textviewFeels.text =
                    getString(R.string.template_feels_like).format(it)
            }

            // weather icon
            // TODO setup weather icon with Coil

            // current condition
            weatherDTO.fact?.condition?.let {
                textviewWeatherCondition.text = getString(resFromCondition(it))
            }
            // prec. probability
            weatherDTO.forecast?.parts?.get(0)?.prec_prob?.let {
                if (it != 0) {
                    textviewPrecProb.visibility = View.VISIBLE
                    imageUmbrella.visibility = View.VISIBLE
                    textviewPrecProb.text =
                        getString(R.string.template_prec_prob).format(it)
                } else {
                    textviewPrecProb.visibility = View.GONE
                    imageUmbrella.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TodayFragment()
    }
}