package activitystarter.compiler.model

import activitystarter.compiler.generation.ConverterGeneration
import activitystarter.compiler.model.classbinding.ClassModel
import activitystarter.compiler.model.param.ParamType
import activitystarter.compiler.processing.ConverterFaktory
import activitystarter.wrapping.ArgConverter
import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

data class ProjectModel (
        val converters: List<ConverterModel> = listOf(),
        val classesToProcess: Set<ClassModel>
) {

    fun converterFor(type: ParamType): ConverterModel? {
        return converters.firstOrNull { it.canWrap(type) }
    }

    fun addUnwrapper(paramType: ParamType, body: () -> String): String {
        val converter = converterFor(paramType) ?: return body()
        return ConverterGeneration(converter).unwrap(body)
    }

    fun addWrapper(paramType: ParamType, body: () -> String): String {
        val converter = converterFor(paramType) ?: return body()
        return ConverterGeneration(converter).wrap(body)
    }
}