@file:Suppress("IllegalIdentifier")

package activitystarter.wrapper

import activitystarter.ActivityStarter
import android.os.Parcelable
import org.junit.Test

class WrapperManagerBuildTest {

    class DateTime
    internal class ParcelableWrapper : ArgWrapper<Any, Parcelable>
    internal class DateTimeWrapper : ArgWrapper<DateTime, Long>

    @Test fun `ArgWrappers can be defined using Builder`() {
        ActivityStarter.setWrapperManager(WrapperManager.Builder().build())
    }

    @Test fun `ArgWrappers can be created by builder with multiple ArgWrappers`() {
        val wrapperManager = WrapperManager.Builder()
                .with(DateTimeWrapper())
                .with(ParcelableWrapper())
                .build()
        ActivityStarter.setWrapperManager(wrapperManager)
    }
}