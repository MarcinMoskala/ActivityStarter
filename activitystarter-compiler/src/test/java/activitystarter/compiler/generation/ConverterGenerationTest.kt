package activitystarter.compiler.generation

import activitystarter.compiler.helpers.ConfigElement
import activitystarter.compiler.processing.ConverterFaktory
import org.junit.Test
import kotlin.test.assertEquals

class ConverterGenerationTest {

    val integerToLongConverter by lazy { ConverterFaktory().create(ConfigElement.integerToLongConverter)[0] }
    val integerToLongConverterGeneration by lazy { ConverterGeneration(integerToLongConverter) }

    val objectToParcelableConverter by lazy { ConverterFaktory().create(ConfigElement.objectToParcelableConverter)[0] }
    val objectToParcelableConverterGeneration by lazy { ConverterGeneration(objectToParcelableConverter) }

    @Test
    fun `Basic wrapping is just creating converter by empty constructor and invoking wrap method`() {
        assertEquals(
                "new com.example.activitystarter.MainActivity.ParcelableConverter().wrap(A)",
                objectToParcelableConverterGeneration.wrap { "A" }
        )
    }

    @Test
    fun `Basic unwrapping is just creating converter by empty constructor and invoking unwrap method`() {
        assertEquals(
                "new com.example.activitystarter.MainActivity.ParcelableConverter().unwrap(A)",
                objectToParcelableConverterGeneration.unwrap { "A" }
        )
    }
}