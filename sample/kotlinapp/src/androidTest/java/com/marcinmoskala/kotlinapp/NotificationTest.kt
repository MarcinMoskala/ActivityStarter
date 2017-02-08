package com.marcinmoskala.kotlinapp


import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
import android.test.suitebuilder.annotation.LargeTest
import com.marcinmoskala.kotlinapp.notification.NotificationPublisher
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class NotificationTest {

    @Rule @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun shouldSeeNotificationTest() {
        val context = InstrumentationRegistry.getTargetContext()
        val intent = MainActivityStarter.getIntent(context)
        activityTestRule.launchActivity(intent)

        val expectedTitle = NotificationPublisher.getTextTitle(MainActivity.notificationId)
        val expectedSubtitle = NotificationPublisher.getTextSubtitle(MainActivity.notificationTime)

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.openNotification()
        device.wait(Until.hasObject(By.text(expectedTitle)), 3000)
        val title = device.findObject(By.text(expectedTitle))
        val text = device.findObject(By.textContains(expectedSubtitle))
        assertEquals(expectedTitle, title.text)
        assertEquals(expectedSubtitle, text.getText())
        title.click()
    }
}
