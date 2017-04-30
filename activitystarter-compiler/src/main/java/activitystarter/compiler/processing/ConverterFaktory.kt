package activitystarter.compiler.processing

import activitystarter.compiler.model.ConverterModel
import activitystarter.compiler.utils.toTypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror


class ConverterFaktory {

    fun create(converters: List<TypeMirror>): List<ConverterModel> {
            return converters.mapNotNull { c ->
                val typeElement = c.toTypeElement() ?: return@mapNotNull null
                val declaredParentInterface = typeElement.interfaces?.get(0) as? DeclaredType
                val genericTypes = declaredParentInterface?.typeArguments ?: return@mapNotNull null
                val fromClass = genericTypes[0]!!
                val toClass = genericTypes[1]!!
                ConverterModel(c.toString(), fromClass, toClass)
            }
    }
}