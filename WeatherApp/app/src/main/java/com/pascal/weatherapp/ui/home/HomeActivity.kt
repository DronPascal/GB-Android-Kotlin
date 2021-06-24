package com.pascal.weatherapp.ui.home

import android.app.Activity
import android.app.SearchManager
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.pascal.weatherapp.R
import com.pascal.weatherapp.app.AppState
import com.pascal.weatherapp.data.model.City
import com.pascal.weatherapp.databinding.HomeActivityBinding
import com.pascal.weatherapp.ui.MainViewModel
import com.pascal.weatherapp.ui.contacts.ContactsActivity
import com.pascal.weatherapp.ui.home.fragments.HomeFragmentsPagerAdapter
import com.pascal.weatherapp.ui.location.LocationActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var connectReceiver: BroadcastReceiver
    private lateinit var topSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)

        initView()

        mainViewModel.initiateWeatherRefresh()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // For SearchView
        intent?.let { handleIntent(it) }
    }

    private fun initView() {
        initToolbar()
        initPager()
        initTabs()
        initSnackbar()
        initFab()
        initReceiver()

        mainViewModel.appStateLiveData.observe(this, {
            when (it) {
                is AppState.Success -> {
                    binding.fab.show()
                }
                is AppState.Loading -> {
                    binding.fab.hide()
                }
                is AppState.Error -> {
                    binding.fab.hide()
                }
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initPager() {
        binding.viewPager.apply {
            adapter = HomeFragmentsPagerAdapter(this@HomeActivity)
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun initTabs() {
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(HomeFragmentsPagerAdapter.TAB_TITLES[position])
        }.attach()
    }

    private fun initFab() {
        binding.fab.setOnClickListener { _ ->
            var weatherMsg: String? = null
            mainViewModel.weatherDtoLiveData.value?.let {
                weatherMsg = "На улице %d℃. Вероятность осадков %d%%.".format(
                    it.fact?.temp, it.forecast?.parts?.get(0)?.prec_prob
                )
            }
            if (weatherMsg != null) {
                val intent = Intent(this, ContactsActivity::class.java)
                intent.putExtra(ContactsActivity.ARGUMENT_WEATHER_MSG, weatherMsg)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error: Unknown weather condition", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initReceiver() {
        connectReceiver = object : ConnectBroadcastReceiver() {
            override fun onNetworkChange(intent: Intent) {
                when (intent.extras?.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
                    false -> topSnackbar.dismiss()
                    true -> topSnackbar.show()
                }
            }
        }
        // TODO CONNECTIVITY_ACTION Deprecated
        @Suppress("DEPRECATION") val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectReceiver, filter)
    }

    private fun initSnackbar() {
        topSnackbar = Snackbar.make(
            binding.root,
            getString(R.string.snackbar_check_connection_msg),
            Snackbar.LENGTH_INDEFINITE
        )
        val params = topSnackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        topSnackbar.view.layoutParams = params
        topSnackbar.setAction(getString(R.string.close)) { topSnackbar.dismiss() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.let { initSearch(it) }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearch(searchItem: MenuItem) {
        val searchView = searchItem.actionView as SearchView
        // TODO Currently this ↙ is the best way to solve search view width issue I found.
        searchView.maxWidth = Int.MAX_VALUE

//        val searchText = (searchView.findViewById(R.id.search_src_text) as SearchView.SearchAutoComplete)
//        searchText.setDropDownBackgroundResource(R.color.white)
//        searchText.setTextColor(getColor(R.color.black))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(this, this::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doSearch(query)
                saveSuggestion(query)
            }
        }
    }

    private fun doSearch(query: String) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show()
    }

    private fun saveSuggestion(query: String) {
        with(SearchSuggestionProvider) {
            SearchRecentSuggestions(this@HomeActivity, AUTHORITY, MODE)
                .saveRecentQuery(query, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_location -> {
                val intent = Intent(this, LocationActivity::class.java)
//                intent.putExtra(ContactsActivity.ARGUMENT_WEATHER_MSG, weatherMsg)
                // TODO use activity result API
                @Suppress("DEPRECATION")
                startActivityForResult(intent, CITY_REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CITY_REQUEST_CODE -> {
                    val city = data?.getParcelableExtra<City>(LocationActivity.RESULT_CITY)
                    city?.let {
                        println(city)
                        saveSuggestion(city.city)
                        mainViewModel.initiateServerWeatherRefresh(city)
                    }
                }
            }
        }
        // TODO use activity result API
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectReceiver)
    }

    companion object {
        const val CITY_REQUEST_CODE = 1003
    }
}
