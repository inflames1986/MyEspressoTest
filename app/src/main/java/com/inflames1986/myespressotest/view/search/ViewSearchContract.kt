package com.inflames1986.myespressotest.view.search

import com.inflames1986.myespressotest.model.SearchResult
import com.inflames1986.myespressotest.view.ViewContract

internal interface ViewSearchContract : ViewContract {
    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}