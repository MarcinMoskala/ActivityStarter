package activitystarter.compiler.model

import activitystarter.compiler.helpers.*
import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.processing.ConverterFaktory
import com.google.testing.compile.CompilationRule
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConverterModelTest() {

    @Rule @JvmField val c = CompilationRule()

    val factory by lazy { ConverterFaktory() }
    val integerToLongConverter by lazy { factory.create(ConfigElement.integerToLongConverter)[0] }
    val objectToParcelableConverter by lazy { factory.create(ConfigElement.objectToParcelableConverter)[0] }

    @Test
    fun `To ParamType is correct`() {
        assertEquals(ParamType.Long, integerToLongConverter.toParamType)
        assertEquals(ParamType.ParcelableSubtype, objectToParcelableConverter.toParamType)
    }

    @Test
    fun `ConverterGeneration can weap simple correct types`() {
        assertTrue(integerToLongConverter.canWrap(c.intTypeMirror))
        assertFalse(integerToLongConverter.canWrap(c.longTypeMirror))
        assertFalse(integerToLongConverter.canWrap(c.objectFTypeMirror))
        assertFalse(integerToLongConverter.canWrap(c.subtypeOfParcelableTypeMirror))
        assertFalse(objectToParcelableConverter.canWrap(c.intTypeMirror))
        assertFalse(objectToParcelableConverter.canWrap(c.longTypeMirror))
        assertTrue(objectToParcelableConverter.canWrap(c.objectFTypeMirror))
        assertFalse(objectToParcelableConverter.canWrap(c.parcelableSubtypeArrayListTypeMirror))
    }

    @Test
    fun `Then convertet include interface then only classes that are implementing it can be wrapped`() {
        assertTrue(ConverterModel("SomeName", c.interfaceETypeMirror, c.intTypeMirror).canWrap(c.objectFTypeMirror))
        assertTrue(ConverterModel("SomeName", c.interfaceBTypeMirror, c.intTypeMirror).canWrap(c.objectFTypeMirror))
        assertTrue(ConverterModel("SomeName", c.interfaceBTypeMirror, c.intTypeMirror).canWrap(c.interfaceETypeMirror))
        assertTrue(ConverterModel("SomeName", c.objectFTypeMirror, c.intTypeMirror).canWrap(c.objectFTypeMirror))
        assertFalse(ConverterModel("SomeName", c.interfaceETypeMirror, c.intTypeMirror).canWrap(c.interfaceBTypeMirror))
    }
}