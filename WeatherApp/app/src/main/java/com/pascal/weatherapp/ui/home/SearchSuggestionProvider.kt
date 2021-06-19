package com.pascal.weatherapp.ui.home

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.pascal.weatherapp.SearchSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}