package com.karkoszka.cookingtime.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.karkoszka.cookingtime.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ZeroCounterTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun zeroCounterTest() {
        val appCompatImageButton = onView(
                allOf(withId(R.id.button4set),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.FrameLayout")),
                                        0),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val appCompatButton = onView(
            allOf(
                withId(R.id.buttonColor),
                withText("Choose color"),
                withContentDescription("Choose color"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.set_time_fragment),
                        1
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val chronometer = onView(
                allOf(withId(R.id.chronometer1), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear1),
                                        childAtPosition(
                                                withId(R.id.frame1),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer.check(doesNotExist())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
