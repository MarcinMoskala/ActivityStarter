package activitystarter.compiler.issubtype

import activitystarter.Arg
import activitystarter.compiler.error
import activitystarter.compiler.getElementType
import activitystarter.compiler.helpers.ParamProcessor
import activitystarter.compiler.isSubtypeOfType
import activitystarter.compiler.messanger
import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class IsSubtypeHelperProcessor(val ofWhat: String) : ParamProcessor() {

    override fun onParamFound(element: Element) {
        try {
            val isSubtype = getElementType(element).isSubtypeOfType(ofWhat)
            if(!isSubtype) error(element, notSubtypeError)
        } catch (e: Exception) {
            e.printStackTrace()
            error(element, otherError)
        }
    }

    companion object {
        val notSubtypeError = "Not a subtype"
        val otherError = "Error"
    }
}