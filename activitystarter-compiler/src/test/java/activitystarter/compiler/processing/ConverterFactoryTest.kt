package activitystarter.compiler.processing

import activitystarter.compiler.helpers.ConfigElement
import org.junit.Test
import kotlin.test.assertEquals

class ConverterFactoryTest() {

    val factory by lazy { ConverterFaktory() }
    val emptyConverters by lazy { factory.create(ConfigElement.empty) }
    val singleConverters by lazy { factory.create(ConfigElement.integerToLongConverter) }

    @Test
    fun `Converter list size is correct`() {
        val empty = ConfigElement.empty
        val singleConverter = ConfigElement.integerToLongConverter
        assertEquals(0, emptyConverters.size)
        assertEquals(1, singleConverters.size)
    }

    @Test
    fun `Converter list class name is correct ofter processing`() {
        assertEquals("com.example.activitystarter.MainActivity.IntToLongConverter", singleConverters[0].className)
    }

    @Test
    fun `Converter list parameter type names are correct ofter processing`() {
        assertEquals("java.lang.Integer", singleConverters[0].typeFrom.toString())
        assertEquals("java.lang.Long", singleConverters[0].typeTo.toString())
    }
}