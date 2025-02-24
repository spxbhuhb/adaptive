package `fun`.adaptive.general

abstract class Observable<VT> {

    abstract var value: VT

    val listeners = mutableListOf<ObservableListener<VT>>()

    fun notifyListeners() {
        for (listener in listeners) listener.onChange(value)
    }

    fun addListener(listener: ObservableListener<VT>) {
        listeners += listener
    }

    fun removeListener(listener: ObservableListener<VT>) {
        listeners -= listener
    }

}