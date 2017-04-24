package activitystarter.compiler.processing

import activitystarter.compiler.helpers.ConfigElement
import org.junit.Test
import kotlin.test.assertEquals

class ConverterFactoryTest() {

    val factory by lazy { ConverterFaktory() }
    val emptyConverters by lazy { factory.create(ConfigElement.empty) }
    val singleConverters by lazy { factory.create(ConfigElement.singleConverter) }

    @Test
    fun `Converter list size is correct`() {
        val empty = ConfigElement.empty
        val singleConverter = ConfigElement.singleConverter
        assertEquals(0, empty.size)
        assertEquals(1, singleConverter.size)
    }

    @Test
    fun `Converter list class name is correct ofter processing`() {
        assertEquals("com.example.activitystarter.MainActivity.IntToLongConverter", singleConverters[0].className)
    }
}