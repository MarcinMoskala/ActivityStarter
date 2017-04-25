package activitystarter.compiler.model

import activitystarter.compiler.helpers.ConfigElement
import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.processing.ConverterFaktory
import org.junit.Test
import kotlin.test.assertEquals

class ConverterModelTest() {

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
        assertEquals(true, integerToLongConverter.canApplyTo(ParamType.Int))
        assertEquals(false, integerToLongConverter.canApplyTo(ParamType.Long))
        assertEquals(false, integerToLongConverter.canApplyTo(ParamType.ParcelableSubtype))
        assertEquals(false, objectToParcelableConverter.canApplyTo(ParamType.Int))
        assertEquals(false, objectToParcelableConverter.canApplyTo(ParamType.Long))
        assertEquals(true, objectToParcelableConverter.canApplyTo(ParamType.ObjectSubtype))
    }
}