package activitystarter.compiler

fun <T> List<T>.createSublists(isSplitter: (T) -> Boolean): List<List<T>> = when {
    size == 0 -> listOf(listOf())
    none { isSplitter(it) } -> listOf(this)
    size == 1 -> listOf(listOf(first()), listOf())
    isSplitter(first()) -> sublistFromRest<T>(isSplitter)
            .flatMap { listOf(it, listOf(first()) + it) }
    else -> sublistFromRest(isSplitter)
            .map { listOf(first()) + it }
}

private fun <T> List<T>.sublistFromRest(isSplitter: (T) -> Boolean) = drop(1).createSublists(isSplitter)