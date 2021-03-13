package com.karkoszka.cookingtime.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityElementsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityElementsTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val linearLayout = onView(
                allOf(withId(R.id.linear1),
                        childAtPosition(
                                allOf(withId(R.id.frame1),
                                        childAtPosition(
                                                withId(R.id.leftLinear),
                                                0)),
                                0),
                        isDisplayed()))
        linearLayout.check(matches(isDisplayed()))

        val imageButton = onView(
                allOf(withId(R.id.button1start),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        imageButton.check(matches(isDisplayed()))

        val imageButton2 = onView(
                allOf(withId(R.id.button1set),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        imageButton2.check(matches(isDisplayed()))

        val textView = onView(
                allOf(withId(R.id.timeInfo1), withText("00:00"),
                        childAtPosition(
                                allOf(withId(R.id.linear1),
                                        childAtPosition(
                                                withId(R.id.frame1),
                                                0)),
                                1),
                        isDisplayed()))
        textView.check(matches(withText("00:00")))

        val chronometer = onView(
                allOf(withId(R.id.chronometer1), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear1),
                                        childAtPosition(
                                                withId(R.id.frame1),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer.check(matches(isDisplayed()))

        val linearLayout2 = onView(
                allOf(withId(R.id.linear2),
                        childAtPosition(
                                allOf(withId(R.id.frame2),
                                        childAtPosition(
                                                withId(R.id.rightLinear),
                                                0)),
                                0),
                        isDisplayed()))
        linearLayout2.check(matches(isDisplayed()))

        val imageButton3 = onView(
                allOf(withId(R.id.button2start),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        imageButton3.check(matches(isDisplayed()))

        val imageButton4 = onView(
                allOf(withId(R.id.button2set),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        imageButton4.check(matches(isDisplayed()))

        val textView2 = onView(
                allOf(withId(R.id.timeInfo2), withText("00:00"),
                        childAtPosition(
                                allOf(withId(R.id.linear2),
                                        childAtPosition(
                                                withId(R.id.frame2),
                                                0)),
                                1),
                        isDisplayed()))
        textView2.check(matches(withText("00:00")))

        val chronometer2 = onView(
                allOf(withId(R.id.chronometer2), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear2),
                                        childAtPosition(
                                                withId(R.id.frame2),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer2.check(matches(isDisplayed()))

        val linearLayout3 = onView(
                allOf(withId(R.id.linear3),
                        childAtPosition(
                                allOf(withId(R.id.frame3),
                                        childAtPosition(
                                                withId(R.id.leftLinear),
                                                1)),
                                0),
                        isDisplayed()))
        linearLayout3.check(matches(isDisplayed()))

        val imageButton5 = onView(
                allOf(withId(R.id.button3start),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        imageButton5.check(matches(isDisplayed()))

        val imageButton6 = onView(
                allOf(withId(R.id.button3set),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        imageButton6.check(matches(isDisplayed()))

        val textView3 = onView(
                allOf(withId(R.id.timeInfo3), withText("00:00"),
                        childAtPosition(
                                allOf(withId(R.id.linear3),
                                        childAtPosition(
                                                withId(R.id.frame3),
                                                0)),
                                1),
                        isDisplayed()))
        textView3.check(matches(withText("00:00")))

        val chronometer3 = onView(
                allOf(withId(R.id.chronometer3), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear3),
                                        childAtPosition(
                                                withId(R.id.frame3),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer3.check(matches(isDisplayed()))

        val linearLayout4 = onView(
                allOf(withId(R.id.linear4),
                        childAtPosition(
                                allOf(withId(R.id.frame4),
                                        childAtPosition(
                                                withId(R.id.rightLinear),
                                                1)),
                                0),
                        isDisplayed()))
        linearLayout4.check(matches(isDisplayed()))

        val imageButton7 = onView(
                allOf(withId(R.id.button4start),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        imageButton7.check(matches(isDisplayed()))

        val imageButton8 = onView(
                allOf(withId(R.id.button4set),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        imageButton8.check(matches(isDisplayed()))

        val textView4 = onView(
                allOf(withId(R.id.timeInfo4), withText("00:00"),
                        childAtPosition(
                                allOf(withId(R.id.linear4),
                                        childAtPosition(
                                                withId(R.id.frame4),
                                                0)),
                                1),
                        isDisplayed()))
        textView4.check(matches(withText("00:00")))

        val chronometer4 = onView(
                allOf(withId(R.id.chronometer4), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear4),
                                        childAtPosition(
                                                withId(R.id.frame4),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer4.check(matches(isDisplayed()))

        val frameLayout = onView(
                allOf(withId(R.id.frame5),
                        childAtPosition(
                                allOf(withId(R.id.leftLinear),
                                        childAtPosition(
                                                withId(R.id.leftFrame),
                                                0)),
                                2),
                        isDisplayed()))
        frameLayout.check(matches(isDisplayed()))

        val imageButton9 = onView(
                allOf(withId(R.id.button5start),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        imageButton9.check(matches(isDisplayed()))

        val imageButton10 = onView(
                allOf(withId(R.id.button5set),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        imageButton10.check(matches(isDisplayed()))

        val textView5 = onView(
                allOf(withId(R.id.timeInfo5), withText("00:00"),
                        childAtPosition(
                                allOf(withId(R.id.linear5),
                                        childAtPosition(
                                                withId(R.id.frame5),
                                                0)),
                                1),
                        isDisplayed()))
        textView5.check(matches(withText("00:00")))

        val chronometer5 = onView(
                allOf(withId(R.id.chronometer5), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear5),
                                        childAtPosition(
                                                withId(R.id.frame5),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer5.check(matches(isDisplayed()))

        val linearLayout5 = onView(
                allOf(withId(R.id.linear6),
                        childAtPosition(
                                allOf(withId(R.id.frame6),
                                        childAtPosition(
                                                withId(R.id.rightLinear),
                                                2)),
                                0),
                        isDisplayed()))
        linearLayout5.check(matches(isDisplayed()))

        val imageButton11 = onView(
                allOf(withId(R.id.button6start),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                0),
                        isDisplayed()))
        imageButton11.check(matches(isDisplayed()))

        val imageButton12 = onView(
                allOf(withId(R.id.button6set),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                1),
                        isDisplayed()))
        imageButton12.check(matches(isDisplayed()))

        val textView6 = onView(
                allOf(withId(R.id.timeInfo6), withText("00:00"),
                        childAtPosition(
                                allOf(withId(R.id.linear6),
                                        childAtPosition(
                                                withId(R.id.frame6),
                                                0)),
                                1),
                        isDisplayed()))
        textView6.check(matches(withText("00:00")))

        val chronometer6 = onView(
                allOf(withId(R.id.chronometer6), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear6),
                                        childAtPosition(
                                                withId(R.id.frame6),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer6.check(matches(isDisplayed()))

        val chronometer7 = onView(
                allOf(withId(R.id.chronometer6), withContentDescription("0 seconds"),
                        childAtPosition(
                                allOf(withId(R.id.linear6),
                                        childAtPosition(
                                                withId(R.id.frame6),
                                                0)),
                                0),
                        isDisplayed()))
        chronometer7.check(matches(isDisplayed()))
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
