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
    fun `ConverterGeneration can weap simple correct types`() {
        assertEquals(true, integerToLongConverter.canWrap(ParamType.Int))
        assertEquals(false, integerToLongConverter.canWrap(ParamType.Long))
        assertEquals(false, integerToLongConverter.canWrap(ParamType.ObjectSubtype))
        assertEquals(false, integerToLongConverter.canWrap(ParamType.ParcelableSubtype))
        assertEquals(false, objectToParcelableConverter.canWrap(ParamType.Int))
        assertEquals(false, objectToParcelableConverter.canWrap(ParamType.Long))
        assertEquals(true, objectToParcelableConverter.canWrap(ParamType.ObjectSubtype))
        assertEquals(false, objectToParcelableConverter.canWrap(ParamType.ParcelableSubtype))
    }
}