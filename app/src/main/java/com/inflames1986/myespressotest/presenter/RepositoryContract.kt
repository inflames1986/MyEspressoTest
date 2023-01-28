package com.inflames1986.myespressotest.presenter

import io.reactivex.Observable
import com.inflames1986.myespressotest.model.SearchResponse
import com.inflames1986.myespressotest.repository.RepositoryCallback
interface RepositoryContract {

    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )

    fun searchGithub(
        query: String
    ): Observable<SearchResponse>

    suspend fun searchGithubAsync(
        query: String
    ): SearchResponse

}