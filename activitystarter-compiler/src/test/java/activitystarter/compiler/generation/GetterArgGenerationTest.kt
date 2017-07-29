package activitystarter.compiler.generation

import activitystarter.compiler.helpers.ConfigElement
import activitystarter.compiler.helpers.getElement
import activitystarter.compiler.processing.ConverterFaktory
import com.google.testing.compile.CompilationRule
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetterArgGenerationTest {

    @Rule @JvmField val c = CompilationRule()

    @Test
    fun `Getting getter returned type`() {
        val elements = c.elements.getTypeElement(ClassWithGetter::class.java.canonicalName).enclosedElements.filter { it.simpleName.startsWith("get") }
        assertEquals(1, elements.size)
    }
}