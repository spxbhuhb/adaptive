package `fun`.adaptive.utility

import `fun`.adaptive.general.Observable

fun <E> MutableList<E>.replaceFirst(newElement: E, findFun : (E) -> Boolean) : MutableList<E> {
    val index = indexOfFirst(findFun)
    if (index != -1) {
        this[index] = newElement
    }
    return this
}

fun <E> Observable<List<E>>.replaceFirst(newElement: E, findFun : (E) -> Boolean) {
    this.value = this.value.toMutableList().also {
        it[it.indexOfFirst(findFun)] = newElement
    }
}