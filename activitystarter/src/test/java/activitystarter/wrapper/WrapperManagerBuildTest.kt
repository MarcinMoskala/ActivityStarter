@file:Suppress("IllegalIdentifier")

package activitystarter.wrapper

import activitystarter.ActivityStarter
import activitystarter.assertSubtype
import android.os.Parcelable
import org.junit.After
import org.junit.Test

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
        assertSubtype(Long::class.javaObjectType, wrapperManager.mappingType(DateTime::class.java))
        assertSubtype(Parcelable::class.java, wrapperManager.mappingType(Any::class.java))
    }

    @Test fun `WrapperManager is mappig super classes as defined`() {

    }

        private fun getBasicWrapperManager(): WrapperManager = WrapperManager
            .Builder()
            .with(WrapperManagerBuildTest.DateTimeWrapper())
            .with(WrapperManagerBuildTest.ParcelableWrapper())
            .build()
}