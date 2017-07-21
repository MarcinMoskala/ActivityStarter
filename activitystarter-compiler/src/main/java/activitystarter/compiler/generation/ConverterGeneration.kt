package activitystarter.compiler.generation

import activitystarter.compiler.model.ConverterModel

class ConverterGeneration(val converter: ConverterModel) {

    fun wrap( f: ()->String): String {
        return "new ${converter.className}().wrap(${f()})"
    }

    fun unwrap( f: ()->String): String {
        return "new ${converter.className}().unwrap(${f()})"
    }
}