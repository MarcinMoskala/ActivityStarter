package com.marcinmoskala.kotlinapp


import android.support.test.InstrumentationRegistry
import android.support.test.filters.SdkSuppress
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
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
