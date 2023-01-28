package com.inflames1986.myespressotest

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inflames1986.myespressotest.model.SearchResponse
import com.inflames1986.myespressotest.presenter.ScheduleProviderStub
import com.inflames1986.myespressotest.repository.FakeGitHubRepository
import com.inflames1986.myespressotest.view.ScreenState
import com.inflames1986.myespressotest.view.search.SearchViewModel
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SearchViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(repository, ScheduleProviderStub())
    }

    @Test
    fun searchViewModel_SuccessResponseTest() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )

        searchViewModel.searchGitHub(SEARCH_QUERY)
        Mockito.verify(repository, Mockito.times(1)).searchGithub(SEARCH_QUERY)
    }


    @Test
    fun searchViewModel_SearchResultNullResponseTest() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    null
                )
            )
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            val expectedValue: String =
                ScreenState
                    .Error(Throwable(ERROR_NULL_SEARCH_RESULT))
                    .error.message.toString()
            val targetValue: String = (liveData.value as ScreenState.Error)
                .error.message.toString()

            Assert.assertEquals(
                expectedValue,
                targetValue
            )
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun searchViewModel_TotalCountNullResponseTest() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            val expectedValue: String =
                ScreenState
                    .Error(Throwable(ERROR_NULL_SEARCH_RESULT))
                    .error.message.toString()
            val targetValue: String = (liveData.value as ScreenState.Error)
                .error.message.toString()

            Assert.assertEquals(
                expectedValue,
                targetValue
            )
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun searchViewModel_TestReturnValueIsNotNull() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()

        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            Assert.assertNotNull(liveData.value)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun searchViewModel_TestReturnValueIsError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)

        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(error)
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            val value: ScreenState.Error = liveData.value as ScreenState.Error
            Assert.assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun searchViewModel_ResponseReturnEmptyTest_LoadingInCycle() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()

        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.empty()
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            Assert.assertEquals(liveData.value, ScreenState.Loading)

        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun searchViewModel_ResponseReturnEmptyTest_NotWorkingNotError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()

        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.empty()
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            Assert.assertNotEquals(liveData.value, ScreenState.Working(mock()))
            Assert.assertNotEquals(liveData.value, ScreenState.Error(mock()))

        } finally {
            liveData.removeObserver(observer)
        }
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val ERROR_NULL_SEARCH_RESULT = "Search results or total count are null"
    }
}