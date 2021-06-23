package com.pascal.weatherapp.ui.home.fragments

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pascal.weatherapp.databinding.HomeFragmentTenDaysBinding
import com.pascal.weatherapp.ui.MainViewModel


class TenDaysFragment : Fragment() {

    private var _binding: HomeFragmentTenDaysBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentTenDaysBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initWebView()

        viewModel.weatherDtoLiveData.observe(viewLifecycleOwner, {
            it.info?.url?.let { url ->
                binding.webView.loadUrl(url)
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(cm: ConsoleMessage): Boolean {
                Log.d(
                    "debug",
                    cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId()
                )
                return true
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                //Toast.makeText(requireContext(), "Страница загружена!", Toast.LENGTH_SHORT).show()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                //Toast.makeText(requireContext(), "Начата загрузка страницы $url", Toast.LENGTH_SHORT).show()
            }
        }
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TenDaysFragment()
    }
}