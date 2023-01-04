package com.inflames1986.myespressotest.presenter

import com.inflames1986.myespressotest.repository.RepositoryCallback

internal interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )
}