package activitystarter.compiler

import activitystarter.Optional
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

class ArgumentBinding(element: Element) {
    val name: String = element.simpleName.toString()
    val elementType: TypeMirror = getElementType(element)
    val type: TypeName = TypeName.get(elementType)
    val isOptional: Boolean = element.getAnnotation(Optional::class.java) != null
    val accessor: FieldAccessor = FieldAccessor(element)
}
