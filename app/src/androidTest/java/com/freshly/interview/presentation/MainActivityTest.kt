package com.freshly.interview.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.freshly.interview.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun main_activity_test() {
        onView(withId(R.id.swipe_to_refresh)).perform(swipeDown())
        // check if progress bar shows on loading
        onView(withId(R.id.pb_progress)).check(matches(isDisplayed()))
        // check if recycler view has data
        onView(withId(R.id.rv_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MainViewHolder>(3, click()))
    }
}