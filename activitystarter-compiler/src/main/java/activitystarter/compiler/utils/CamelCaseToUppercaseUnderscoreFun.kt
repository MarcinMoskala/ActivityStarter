package activitystarter.compiler.utils

fun camelCaseToUppercaseUnderscore(str: String): String = str
        .replace("([A-Z])".toRegex(), "_\$1")
        .toUpperCase()
        .deleteIfFirst('_')

private fun String.deleteIfFirst(c: Char): String = if(!isEmpty() && get(0) == c) drop(1) else this
