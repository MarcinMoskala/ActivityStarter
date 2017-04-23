@file:Suppress("IllegalIdentifier")

package activitystarter.wrapper

import activitystarter.ActivityStarter
import activitystarter.wrapping.ArgWrapper
import activitystarter.assertSubtype
import android.accounts.Account
import android.os.Parcel
import android.os.Parcelable
import org.junit.After
import org.junit.Assert
import org.junit.Test

class WrapperManagerTests {


    private val exampleObject by lazy { object {} }
    private val otherObject by lazy { object {} }
    private val exampleParcelable by lazy { StudentParcelable(0) }
    private val otherParcelable by lazy { StudentParcelable(1) }

    private val exampleDateTime by lazy { DateTime() }
    private val otherDateTime by lazy { DateTime() }
    private val exampleLong by lazy { 0L }

    private open class DateTime
    private inner class ParcelableWrapper : ArgWrapper<Any, Parcelable> {
        override fun requiredAnnotation(): Class<out Annotation>? = null
        override fun wrap(toWrap: Any): Parcelable = if (toWrap == exampleObject) exampleParcelable else otherParcelable
        override fun unwrap(wrapped: Parcelable): Any = if (wrapped == exampleParcelable) exampleObject else otherObject
    }

    private inner class DateTimeWrapper : ArgWrapper<DateTime, Long> {
        override fun requiredAnnotation(): Class<out Annotation>? = null
        override fun wrap(toWrap: DateTime): Long = if (toWrap == exampleDateTime) exampleLong else -1L
        override fun unwrap(toUnwrap: Long): DateTime = if (toUnwrap == exampleLong) exampleDateTime else otherDateTime
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
        Assert.assertEquals(exampleLong, wrapperManager.wrap(exampleDateTime))
        Assert.assertEquals(exampleParcelable, wrapperManager.wrap(exampleObject))
    }


    @Test fun `WrapperManager is unwrapping as defined`() {
        val wrapperManager = getBasicWrapperManager()
        Assert.assertEquals(exampleDateTime, wrapperManager.unwrap(exampleLong))
        Assert.assertEquals(exampleObject, wrapperManager.unwrap(exampleParcelable))
    }

    private fun getBasicWrapperManager(): WrapperManager = WrapperManager
            .Builder()
            .with(DateTimeWrapper())
            .with(ParcelableWrapper())
            .build()
}