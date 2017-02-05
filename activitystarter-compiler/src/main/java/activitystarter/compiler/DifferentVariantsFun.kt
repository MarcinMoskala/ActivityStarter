package activitystarter.compiler

fun <T> List<T>.createSublists(isSplitter: (T) -> Boolean): List<List<T>> = when {
    size == 0 -> listOf()
    none { isSplitter(it) } -> listOf(this)
    size == 1 -> listOf(listOf(first()), listOf())
    isSplitter(first()) -> drop(1).createSublists(isSplitter)
            .flatMap { listOf(listOf(first()) + it, it) }
    else -> drop(1).createSublists(isSplitter)
            .map { listOf(first()) + it }
}