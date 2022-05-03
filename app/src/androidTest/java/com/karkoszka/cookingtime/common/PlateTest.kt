package com.karkoszka.cookingtime.common

import org.junit.Assert

internal class PlateTest {

    @org.junit.Test
    fun formatAlarmInfoTextTest() {
        Assert.assertEquals("01:01:01", Plate.formatAlarmInfoText(1,1,1))
        Assert.assertEquals("00:01:01", Plate.formatAlarmInfoText(0,1,1))
    }
}