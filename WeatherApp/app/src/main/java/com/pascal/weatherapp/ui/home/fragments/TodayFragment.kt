package com.pascal.weatherapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.databinding.HomeFragmentTodayBinding
import com.pascal.weatherapp.ui.home.HomeActivity
import com.pascal.weatherapp.utils.showSnackBar


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
        binding.contentLayout.visibility = View.VISIBLE
        viewModel.weatherDtoLiveData.observe(viewLifecycleOwner, {
            displayWeather(it)
        })

        viewModel.appStateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is AppState.Success -> {
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                }
                is AppState.Loading -> {
                    binding.contentLayout.visibility = View.GONE
                    binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Error -> {
                    it.error.message?.let { msg -> binding.root.showSnackBar(msg) }
                }
            }
        })
    }

    private fun initView() {
        initRefresh()
    }

    private fun initRefresh() {
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            contentLayout.visibility = View.VISIBLE
            includedLoadingLayout.loadingLayout.visibility = View.GONE

            weatherDTO.now?.let {
                textviewTime.text = it.toString()
            }
            weatherDTO.forecast?.parts?.get(0)?.let {
                textviewDayNightTemp.text = "${it.temp_max} ${it.temp_min}"
            }
            weatherDTO.fact?.temp?.let {
                textviewTemp.text = it.toString()
            }
            weatherDTO.fact?.feels_like?.let {
                textviewFeels.text = it.toString()
            }

            // TODO setup weather icon with Coil
            weatherDTO.fact?.condition?.let {
                textviewWeatherCondition.text = it
            }
            weatherDTO.forecast?.parts?.get(0)?.prec_prob?.let {
                textviewPrecProb.text = it.toString()
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