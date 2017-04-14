@file:Suppress("IllegalIdentifier")

package activitystarter.wrapper

import activitystarter.ActivityStarter
import activitystarter.assertSubtype
import android.accounts.Account
import android.os.Parcel
import android.os.Parcelable
import org.junit.After
import org.junit.Assert
import org.junit.Test

class WrapperManagerTests {


    private val exampleObject by lazy { Unit }
    private val exampleParcelable by lazy { StudentParcelable(0) }
    private val otherParcelable by lazy { StudentParcelable(1) }

    private val exampleDateTime by lazy { DateTime() }
    private val exampleLong by lazy { 0L }

    private open class DateTime
    private inner class ParcelableWrapper : ArgWrapper<Any, Parcelable> {
        override fun wrap(toWrap: Any?): Parcelable = if (toWrap == exampleObject) exampleParcelable else otherParcelable
    }

    private inner class DateTimeWrapper : ArgWrapper<DateTime, Long> {
        override fun wrap(toWrap: DateTime?): Long = if (toWrap == exampleDateTime) exampleLong else -1L
    }

    private class ExampleDateTime : DateTime()
    private class ExampleClass

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
        val wrapperManager = getBasicWrapperManager()
        assertSubtype(Long::class.javaObjectType, wrapperManager.mappingType(ExampleDateTime::class.java))
        assertSubtype(Parcelable::class.java, wrapperManager.mappingType(ExampleClass::class.java))
    }

    @Test fun `WrapperManager is wrapping as defined`() {
        val wrapperManager = getBasicWrapperManager()
        Assert.assertEquals(exampleLong, wrapperManager.wrap(exampleDateTime) as Long?)
        Assert.assertEquals(exampleParcelable, wrapperManager.wrap(exampleObject) as Parcelable?)
    }

    private fun getBasicWrapperManager(): WrapperManager = WrapperManager
            .Builder()
            .with(DateTimeWrapper())
            .with(ParcelableWrapper())
            .build()
}