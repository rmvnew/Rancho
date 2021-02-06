package com.example.rancho.util

import junit.framework.TestCase

class DateUtilTest : TestCase() {

    fun testGetCurrentDate() {
        var currentDate = DateUtil.getCurrentDate()
        assertEquals("05/02/2021",currentDate)
    }

}