package activitystarter.compiler.processing

import activitystarter.compiler.helpers.ConfigElement
import org.junit.Test
import kotlin.test.assertEquals

class ConverterFactoryTest() {

    val factory = ConverterFaktory()

    @Test
    fun `Converter list size is correct`() {
        val empty = ConfigElement.empty
        val singleConverter = ConfigElement.singleConverter
        assertEquals(0, empty.size)
        assertEquals(1, singleConverter.size)
    }
}