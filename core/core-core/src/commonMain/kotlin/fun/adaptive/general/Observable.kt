package `fun`.adaptive.general

import kotlin.reflect.KProperty

abstract class Observable<VT> {

    abstract var value: VT

    val listeners = mutableListOf<ObservableListener<VT>>()

    fun notifyListeners() {
        for (listener in listeners) listener.onChange(value)
    }

    open fun addListener(listener: ObservableListener<VT>) {
        listeners += listener
    }

    open fun removeListener(listener: ObservableListener<VT>) {
        listeners -= listener
    }

    @Suppress("unused")
    open fun <PT> notify(property: KProperty<*>, oldValue: PT, newValue: PT) {
        notifyListeners()
    }

}