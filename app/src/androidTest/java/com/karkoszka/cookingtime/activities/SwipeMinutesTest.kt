package com.karkoszka.cookingtime.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.karkoszka.cookingtime.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SwipeMinutesTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun swipeMinutesTest() {
        val appCompatImageButton = onView(
            allOf(
                withId(R.id.button3set), withContentDescription("Set"),
                childAtPosition(
                    allOf(
                        withId(R.id.buttonLayout3),
                        childAtPosition(
                            withId(R.id.main_fragment),
                            16
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        fun swipeRight(): ViewAction? {
            return GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.THUMB
            )
        }

        onView(withId(R.id.seekBarMinutes)).perform(swipeRight())

        val textView = onView(
            allOf(
                withId(R.id.dynamicTextMinutes), withText("59"),
                withParent(
                    allOf(
                        withId(R.id.infoPanel),
                        withParent(withId(R.id.linearSplitSetTime))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("59")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

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
