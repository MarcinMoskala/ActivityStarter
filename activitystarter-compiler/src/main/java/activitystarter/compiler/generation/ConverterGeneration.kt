package activitystarter.compiler.generation

import activitystarter.compiler.model.ConverterModel

class ConverterGeneration(val converter: ConverterModel) {

    fun wrap( f: ()->String): String {
        val converterName = converter.className
        return "new $converterName().wrap(${f()})"
    }

    fun unwrap( f: ()->String): String {
        return f()
    }
}