package activitystarter.compiler.model

import activitystarter.compiler.helpers.*
import activitystarter.compiler.processing.ConverterFaktory
import com.google.testing.compile.CompilationRule
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProjectConfigConvertersTest() {

    @Rule @JvmField val c = CompilationRule()

    val factory by lazy { ConverterFaktory() }

    @Test
    fun `There is correct converter applied for simple examples`() {
        val model = ProjectConfig(
                converters = factory.create(ConfigElement.multipleConverter)
        )
        assertNull(model.converterFor(c.boolTypeMirror))
        assertNull(model.converterFor(c.longTypeMirror))
        assertNull(model.converterFor(c.floatTypeMirror))
        assertEquals(
                "com.example.activitystarter.MainActivity.IntToLongConverter",
                model.converterFor(c.intTypeMirror)?.className
        )
        assertEquals(
                "com.example.activitystarter.MainActivity.ParcelableConverter",
                model.converterFor(c.objectFTypeMirror)?.className
        )
    }
}