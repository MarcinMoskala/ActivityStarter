package activitystarter.compiler.utils

fun camelCaseToUppercaseUnderscore(str: String): String = str
        .replace("([A-Z])".toRegex(), "_\$1")
        .toUpperCase()