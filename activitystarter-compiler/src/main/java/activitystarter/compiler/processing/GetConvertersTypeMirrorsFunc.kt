package activitystarter.compiler.processing

import activitystarter.ActivityStarterConfig
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

fun getConvertersTypeMirrors(element: Element): List<TypeMirror> {
    val convertersValue: AnnotationValue = element.annotationMirrors
            .filter { it.annotationType.toString() == ActivityStarterConfig::class.java.name }
            .flatMap { it.elementValues.toList() }
            .firstOrNull { (k, _) -> k.simpleName.toString() == "converters" }?.second ?: return emptyList()
    val valueList = convertersValue.value as List<AnnotationValue>
    return valueList.map { it.value as TypeMirror }
}
