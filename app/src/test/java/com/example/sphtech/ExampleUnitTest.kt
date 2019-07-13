package com.example.sphtech

import androidx.test.rule.ActivityTestRule
import com.example.sphtech.activity.MainActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @Rule
    @JvmField
    val rule : ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    @Before
    fun setUp() {
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

}
