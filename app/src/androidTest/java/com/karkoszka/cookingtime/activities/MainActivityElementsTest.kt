package com.karkoszka.cookingtime.activities


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.karkoszka.cookingtime.R
import org.hamcrest.Matchers.allOf
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
    fun mainActivityElementsNew() {
        val chronometer = onView(
allOf(withId(R.id.chronometer1), withText("00:00"), withContentDescription("0 seconds"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        chronometer.check(matches(isDisplayed()))
        
        val textView = onView(
allOf(withId(R.id.timeInfo1), withText("00:00:00"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        textView.check(matches(withText("00:00:00")))
        
        val imageButton = onView(
allOf(withId(R.id.button1start), withContentDescription("Start"),
withParent(allOf(withId(R.id.buttonLayout1),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton.check(matches(isDisplayed()))
        
        val imageButton2 = onView(
allOf(withId(R.id.button1set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout1),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton2.check(matches(isDisplayed()))
        
        val chronometer2 = onView(
allOf(withId(R.id.chronometer2), withText("00:00"), withContentDescription("0 seconds"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        chronometer2.check(matches(isDisplayed()))
        
        val imageButton3 = onView(
allOf(withId(R.id.button2start), withContentDescription("Start"),
withParent(allOf(withId(R.id.buttonLayout2),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton3.check(matches(isDisplayed()))
        
        val imageButton4 = onView(
allOf(withId(R.id.button2set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout2),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton4.check(matches(isDisplayed()))
        
        val chronometer3 = onView(
allOf(withId(R.id.chronometer3), withText("00:00"), withContentDescription("0 seconds"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        chronometer3.check(matches(isDisplayed()))
        
        val textView2 = onView(
allOf(withId(R.id.timeInfo3), withText("00:00:00"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        textView2.check(matches(withText("00:00:00")))
        
        val imageButton5 = onView(
allOf(withId(R.id.button3start), withContentDescription("Start"),
withParent(allOf(withId(R.id.buttonLayout3),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton5.check(matches(isDisplayed()))
        
        val imageButton6 = onView(
allOf(withId(R.id.button3set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout3),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton6.check(matches(isDisplayed()))
        
        val chronometer4 = onView(
allOf(withId(R.id.chronometer4), withText("00:00"), withContentDescription("0 seconds"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        chronometer4.check(matches(isDisplayed()))
        
        val textView3 = onView(
allOf(withId(R.id.timeInfo4), withText("00:00:00"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        textView3.check(matches(withText("00:00:00")))
        
        val imageButton7 = onView(
allOf(withId(R.id.button4start), withContentDescription("Start"),
withParent(allOf(withId(R.id.buttonLayout4),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton7.check(matches(isDisplayed()))
        
        val imageButton8 = onView(
allOf(withId(R.id.button4set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout4),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton8.check(matches(isDisplayed()))
        
        val chronometer5 = onView(
allOf(withId(R.id.chronometer5), withText("00:00"), withContentDescription("0 seconds"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        chronometer5.check(matches(isDisplayed()))
        
        val textView4 = onView(
allOf(withId(R.id.timeInfo5), withText("00:00:00"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        textView4.check(matches(withText("00:00:00")))
        
        val imageButton9 = onView(
allOf(withId(R.id.button5start), withContentDescription("Start"),
withParent(allOf(withId(R.id.buttonLayout5),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton9.check(matches(isDisplayed()))
        
        val imageButton10 = onView(
allOf(withId(R.id.button5set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout5),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton10.check(matches(isDisplayed()))
        
        val chronometer6 = onView(
allOf(withId(R.id.chronometer6), withText("00:00"), withContentDescription("0 seconds"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        chronometer6.check(matches(isDisplayed()))
        
        val textView5 = onView(
allOf(withId(R.id.timeInfo6), withText("00:00:00"),
withParent(allOf(withId(R.id.main_fragment),
withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)))),
isDisplayed()))
        textView5.check(matches(withText("00:00:00")))
        
        val imageButton11 = onView(
allOf(withId(R.id.button6start), withContentDescription("Start"),
withParent(allOf(withId(R.id.buttonLayout6),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton11.check(matches(isDisplayed()))
        
        val imageButton12 = onView(
allOf(withId(R.id.button6set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout6),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton12.check(matches(isDisplayed()))
        
        val imageButton13 = onView(
allOf(withId(R.id.button6set), withContentDescription("Set"),
withParent(allOf(withId(R.id.buttonLayout6),
withParent(withId(R.id.main_fragment)))),
isDisplayed()))
        imageButton13.check(matches(isDisplayed()))
        }
    }
