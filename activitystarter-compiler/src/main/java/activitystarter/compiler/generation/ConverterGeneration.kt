package activitystarter.compiler.generation

import activitystarter.compiler.model.ConverterModel

class ConverterGeneration(val converter: ConverterModel) {

    val converterName = converter.className

    fun wrap( f: ()->String): String {
        return "new $converterName().wrap(${f()})"
    }

    fun unwrap( f: ()->String): String {
        return "new $converterName().unwrap(${f()})"
    }
}