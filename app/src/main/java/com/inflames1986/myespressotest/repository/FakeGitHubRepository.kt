package com.inflames1986.myespressotest.repository

import retrofit2.Response
import com.inflames1986.myespressotest.model.SearchResponse
import com.inflames1986.myespressotest.presenter.RepositoryContract


internal class FakeGitHubRepository : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        callback.handleGitHubResponse(Response.success(SearchResponse(42, listOf())))
    }
}