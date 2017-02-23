package activitystarter.compiler

import activitystarter.Arg
import activitystarter.Optional
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

class ArgumentBinding(element: Element) {
    val name: String = element.simpleName.toString()
    val key: String
    val elementType: TypeMirror = getElementType(element)
    val type: TypeName = TypeName.get(elementType)
    val isOptional: Boolean = element.getAnnotation(Optional::class.java) != null
    val accessor: FieldAccessor = FieldAccessor(element)

    init {
        val keyFromAnnotation = element.getAnnotation(Arg::class.java)?.key
        val defaultKey = name + "StarterKey"
        key = if(keyFromAnnotation.isNullOrBlank()) defaultKey else keyFromAnnotation!!
    }
}
