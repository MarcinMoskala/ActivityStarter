package activitystarter.compiler.model

import activitystarter.compiler.helpers.ConfigElement
import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.processing.ConverterFaktory
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProjectModelConvertersTest() {

    val factory by lazy { ConverterFaktory() }

    @Test
    fun `There is correct converter applied for simple examples`() {
        val model = ProjectModel(
                converters = factory.create(ConfigElement.multipleConverter),
                classesToProcess = setOf()
        )
        assertNull(model.converterFor(ParamType.Boolean))
        assertNull(model.converterFor(ParamType.Long))
        assertNull(model.converterFor(ParamType.Float))
        assertEquals(
                "com.example.activitystarter.MainActivity.IntToLongConverter",
                model.converterFor(ParamType.Int)?.className
        )
        assertEquals(
                "com.example.activitystarter.MainActivity.ParcelableConverter",
                model.converterFor(ParamType.ObjectSubtype)?.className
        )
    }
}