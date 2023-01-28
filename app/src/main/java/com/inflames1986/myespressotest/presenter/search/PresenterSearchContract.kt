package com.inflames1986.myespressotest.presenter.search

import com.inflames1986.myespressotest.presenter.PresenterContract

internal interface PresenterSearchContract : PresenterContract {
    fun searchGitHub(searchQuery: String)
}