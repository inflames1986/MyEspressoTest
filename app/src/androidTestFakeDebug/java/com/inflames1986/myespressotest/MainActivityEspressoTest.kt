package com.inflames1986.myespressotest

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.inflames1986.myespressotest.view.search.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText(QUERY), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        onView(withId(R.id.totalCountTextView)).check(matches(withText(NUMBER_RESULT)))
    }

    private fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = WAIT_TEXT
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(DELAY)
            }
        }
    }

    @Test
    fun activitySearch_AssertNotNull_ReturnsTrue() {
        scenario.onActivity {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun activitySearch_isInResumeState_ReturnsTrue() {
        Assert.assertEquals(scenario.state, Lifecycle.State.RESUMED)
    }

    @Test
    fun activitySearch_EditTextIsVisible_ReturnsTrue() {
        onView(withId(R.id.searchEditText)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun activitySearch_EditTextIsOnScreen_ReturnsTrue() {
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun activitySearch_isButtonVisible_ReturnsTrue() {
        onView(withId(R.id.toDetailsActivityButton)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    @Test
    fun activitySearch_isOnScreen_ReturnsTrue() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(isDisplayed()))
    }

    @Test
    fun activitySearch_ButtonTextIsOk_ReturnsTrue() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText(BUTTON_TEXT)))
    }

    @Test
    fun activitySearch_EditText_HintTextIsCorrect_ReturnsTrue() {
        onView(withId(R.id.searchEditText)).check(matches(withHint(SEARCH_HINT)))
    }

    companion object {
        private const val NUMBER_RESULT = "Number of results: 42"
        private const val QUERY = "algol"
        private const val WAIT_TEXT = "wait for 2 seconds"
        private const val DELAY = 2000L
        private const val BUTTON_TEXT = "TO DETAILS"
        private const val SEARCH_HINT = "Enter keyword e.g. android"
    }
}