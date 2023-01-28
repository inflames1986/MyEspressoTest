package com.inflames1986.myespressotest.presenter.search

import com.inflames1986.myespressotest.presenter.PresenterContract

/**
 *   Project: EspressoTest
 *
 *   Package: softing.ubah4ukdev.espressotest.presenter.search
 *
 *   Created by Ivan Sheynmaer
 *
 *   Description:
 *
 *
 *   2021.11.04
 *
 *   v1.0
 */
internal interface PresenterSearchContract : PresenterContract {
    fun searchGitHub(searchQuery: String)
}