package activitystarter.compiler.issubtype

import activitystarter.compiler.error.error
import activitystarter.compiler.utils.getElementType
import activitystarter.compiler.utils.isSubtypeOfType
import com.google.auto.service.AutoService
import javax.annotation.processing.Processor
import javax.lang.model.element.Element

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