package activitystarter.compiler.model

import jdk.nashorn.internal.codegen.types.Type

data class ConverterModel(
        val className: String,
        val typeFrom: Type,
        val typeTo: Type
)