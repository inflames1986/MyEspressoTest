package softing.ubah4ukdev.espressotest

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import softing.ubah4ukdev.espressotest.view.details.DetailsActivity

/**
 *   Project: EspressoTest
 *
 *   Package: softing.ubah4ukdev.espressotest
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
@RunWith(AndroidJUnit4::class)
class DetailsActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<DetailsActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTextView_HasText() {
        val assertion = ViewAssertions.matches(ViewMatchers.withText("Number of results: 0"))
        Espresso.onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun activityTextView_IsDisplayed() {
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun activityTextView_IsCompletelyDisplayed() {
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
    }

    @Test
    fun activityButtons_AreEffectiveVisible() {
        Espresso.onView(withId(R.id.incrementButton))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.decrementButton))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun activityButtonIncrement_IsWorking() {
        Espresso.onView(withId(R.id.incrementButton)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("Number of results: 1")))
    }

    @Test
    fun activityButtonDecrement_IsWorking() {
        Espresso.onView(withId(R.id.decrementButton)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("Number of results: -1")))
    }

    @After
    fun close() {
        scenario.close()
    }
}