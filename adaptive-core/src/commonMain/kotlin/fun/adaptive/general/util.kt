package `fun`.adaptive.general

fun <E> Observable<List<E>>.replaceFirst(newElement: E, findFun : (E) -> Boolean) {
    this.value = this.value.toMutableList().also {
        it[it.indexOfFirst(findFun)] = newElement
    }
}