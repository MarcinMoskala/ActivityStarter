package activitystarter.compiler.processing

import activitystarter.ActivityStarterConfig
import activitystarter.compiler.model.ConverterModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror


class ConverterFaktory {

    fun create(configAnnotation: ActivityStarterConfig): List<ConverterModel> {
        try {
            val converters = configAnnotation.converters as Array<TypeMirror>
            return emptyList()
//            return converters.map { converter ->
//                val javaClass = converter.java
//                val genericTypes = javaClass.typeParameters
//                val fromClass: Type = genericTypes[0]
//                val toClass: Type = genericTypes[1]
//                ConverterModel(javaClass.simpleName, fromClass, toClass)
//            }
        } catch (mte: MirroredTypeException) {
            val baggerClass = mte.typeMirror
            return emptyList()
        }
    }
}