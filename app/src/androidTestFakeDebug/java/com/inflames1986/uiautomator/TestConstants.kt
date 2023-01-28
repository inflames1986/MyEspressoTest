package com.inflames1986.uiautomator

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

object TestConstants {
    const val TIMEOUT = 10000L
    const val DELAY = 4000L
    const val WAIT_DESCRIPTION = "wait for $4 seconds"
    const val FIND_BUTTON = "find_button"
    const val TOTAL_COUNT = "totalCountTextView"
    const val FIND_RESULT_STRING = "Number of results: 688"
    const val QUERY = "AppDelivery"
    const val QUERY_SECOND = "UiAutomator"
    const val SEARCH_EDIT_TEXT_ID = "searchEditText"
    const val TO_DETAILS_BUTTON_ID = "toDetailsActivityButton"
    const val FIND_RESULT_INCREMENT = "Number of results: 1"
    const val FIND_RESULT_DECREMENT = "Number of results: -1"
    const val RESULT_EMPTY = "Number of results: 0"
    const val RESULT_FORTY_TWO = "Number of results: 42"
    const val INCREMENT_BUTTON_ID = "incrementButton"
    const val DECREMENT_BUTTON_ID = "decrementButton"
    const val PROGRESS_BAR_ID = "progressBar"
    const val SETTINGS = "Настройки"
    const val SWIPE_START_X = 500
    const val SWIPE_START_Y = 1500
    const val SWIPE_END_X = 500
    const val SWIPE_END_Y = 0
    const val SWIPE_STEPS = 5
    const val SETTING_PACKAGE = "com.android.settings"
    const val EXACT_DEPT = 0

    fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = WAIT_DESCRIPTION
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(DELAY)
            }
        }
    }
}