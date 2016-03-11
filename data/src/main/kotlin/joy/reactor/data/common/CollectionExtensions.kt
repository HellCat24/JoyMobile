package  joy.reactor.data.common

/**
 * Created by y2k on 1/31/16.
 */

fun <T> MutableList<T>.unionOrdered(other: List<T>): List<T> {
    val result = this
    result.addAll(other)
    return result
}

fun <T> List<T>.groupToPair(): List<Pair<T, T>> {
    return withIndex()
        .groupBy { it.index and 2 }
        .map { it.value[0].value to it.value[1].value }
}