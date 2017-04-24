package activitystarter.compiler.processing

import activitystarter.ActivityStarterConfig
import activitystarter.compiler.model.ConverterModel
import activitystarter.compiler.utils.isSubtypeOfType
import activitystarter.compiler.utils.toTypeElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror


class ConverterFaktory {

    fun create(converters: List<TypeMirror>): List<ConverterModel> {
            return converters.mapNotNull { c ->
                val genericTypes = (c.toTypeElement()?.interfaces?.get(0) as? DeclaredType)?.typeArguments
                genericTypes ?: return@mapNotNull null
                val fromClass = genericTypes[0]!!.toString()
                val toClass = genericTypes[1]!!.toString()
                ConverterModel(c.toString(), fromClass, toClass)
            }
    }
}