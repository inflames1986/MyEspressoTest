package com.inflames1986.myespressotest.repository

import retrofit2.Response
import com.inflames1986.myespressotest.model.SearchResponse

interface RepositoryCallback {
    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}