package `fun`.adaptive.general

fun interface ObservableListener<VT> {
    fun onChange(value: VT)
}