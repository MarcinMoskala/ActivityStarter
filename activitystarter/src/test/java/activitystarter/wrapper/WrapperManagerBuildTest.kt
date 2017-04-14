@file:Suppress("IllegalIdentifier")

package activitystarter.wrapper

import activitystarter.ActivityStarter
import android.accounts.Account
import android.graphics.Color
import android.os.Parcelable
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.io.Serializable

class WrapperManagerBuildTest {

    class DateTime
    internal class ParcelableWrapper : ArgWrapper<Any, Parcelable>
    internal class DateTimeWrapper : ArgWrapper<DateTime, Long>

    @After fun tearDown() {
        ActivityStarter.setWrapperManager(null)
    }

    @Test fun `ArgWrappers can be defined using Builder`() {
        ActivityStarter.setWrapperManager(WrapperManager.Builder().build())
    }

    @Test fun `ArgWrappers can be created by builder with multiple ArgWrappers`() {
        val wrapperManager = getBasicWrapperManager()
        ActivityStarter.setWrapperManager(wrapperManager)
    }

    @Test fun `WrapperManager is mappig base classes as defined`() {
        val wrapperManager = getBasicWrapperManager()
    }

    private fun getBasicWrapperManager(): WrapperManager = WrapperManager
            .Builder()
            .with(WrapperManagerBuildTest.DateTimeWrapper())
            .with(WrapperManagerBuildTest.ParcelableWrapper())
            .build()
}