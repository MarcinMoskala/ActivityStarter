package com.marcinmoskala.kotlinapp

import androidx.test.InstrumentationRegistry
import androidx.test.filters.SdkSuppress
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.marcinmoskala.kotlinapp.notification.NotificationPublisher
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class NotificationTest {

    @Rule @JvmField var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun shouldSeeNotificationTest() {
        activityTestRule.launchActivity(MainActivityStarter.getIntent(InstrumentationRegistry.getTargetContext()))

        val expectedTitle = NotificationPublisher.getTextTitle(MainActivity.notificationId)
        val expectedSubtitle = NotificationPublisher.getTextSubtitle(MainActivity.notificationTime)

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.openNotification()
        device.wait(Until.hasObject(By.text(expectedTitle)), 5000)
        assertEquals(expectedTitle, device.findObject(By.text(expectedTitle)).text)
        assertEquals(expectedSubtitle, device.findObject(By.textContains(expectedSubtitle)).text)
        device.pressBack()
    }
}
