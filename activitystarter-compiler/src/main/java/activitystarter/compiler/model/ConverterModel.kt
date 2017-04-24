package activitystarter.compiler.model

import java.lang.reflect.Type


data class ConverterModel(
        val className: String,
        val typeFrom: String,
        val typeTo: String
)