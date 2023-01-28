package com.inflames1986.myespressotest.view

import com.inflames1986.myespressotest.model.SearchResponse

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}