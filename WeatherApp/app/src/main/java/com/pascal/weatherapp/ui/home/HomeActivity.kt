package com.pascal.weatherapp.ui.home

import android.app.SearchManager
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.pascal.weatherapp.R
import com.pascal.weatherapp.databinding.HomeActivityBinding
import com.pascal.weatherapp.ui.MainViewModel
import com.pascal.weatherapp.ui.home.fragments.FragmentsPagerAdapter


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var receiver: BroadcastReceiver
    private lateinit var snackbar: Snackbar

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
        intent?.let { handleIntent(it) }
    }

    private fun initView() {
        initToolbar()
        initPager()
        initTabs()
        initSnackbar()
        initFab()
        initReceiver()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initPager() {
        binding.viewPager.apply {
            adapter = FragmentsPagerAdapter(this@HomeActivity)
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun initTabs() {
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(FragmentsPagerAdapter.TAB_TITLES[position])
        }.attach()
    }

    private fun initFab() {
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "No action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun initReceiver() {
        receiver = object : ConnectBroadcastReceiver() {
            override fun onNetworkChange(intent: Intent) {
                when (intent.extras?.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
                    false -> snackbar.dismiss()
                    true -> snackbar.show()
                }
            }
        }
        // TODO CONNECTIVITY_ACTION Deprecated
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, filter)
    }

    private fun initSnackbar() {
        snackbar = Snackbar.make(
            binding.root,
            getString(R.string.snackbar_check_connection_msg),
            Snackbar.LENGTH_INDEFINITE
        )
        val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackbar.view.layoutParams = params
        snackbar.setAction(getString(R.string.close)) { snackbar.dismiss() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.let { initSearch(it) }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearch(searchItem: MenuItem) {
        val searchView = searchItem.actionView as SearchView
        // TODO Currently this ↙ is the best way to solve search view width issue.
        searchView.maxWidth = Int.MAX_VALUE

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
            val query = intent.getStringExtra(SearchManager.QUERY)
            query?.let { doSearch(it) }
        }
    }

    private fun doSearch(query: String) {}

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
