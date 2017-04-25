package activitystarter.compiler.model

import activitystarter.compiler.helpers.*
import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.processing.ConverterFaktory
import com.google.testing.compile.CompilationRule
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

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
    fun `ConverterGeneration is accepting correct types to unpack`() {
        assertEquals(false, integerToLongConverter.canApplyTo(c.intTypeMirror))
        assertEquals(true, integerToLongConverter.canApplyTo(c.longTypeMirror))
        assertEquals(false, integerToLongConverter.canApplyTo(c.subtypeOfParcelableTypeMirror))
        assertEquals(false, objectToParcelableConverter.canApplyTo(c.intTypeMirror))
        assertEquals(false, objectToParcelableConverter.canApplyTo(c.longTypeMirror))
        assertEquals(true, objectToParcelableConverter.canApplyTo(c.subtypeOfParcelableTypeMirror))
    }
}