package `fun`.adaptive.general

import kotlin.reflect.KProperty

interface Observable<VT> {

    var value: VT

    val listeners : MutableList<ObservableListener<VT>>

    fun notifyListeners() {
        for (listener in listeners) listener.onChange(value)
    }

    fun addListener(listener: ObservableListener<VT>) {
        listeners += listener
    }

    fun removeListener(listener: ObservableListener<VT>) {
        listeners -= listener
    }

    @Suppress("unused")
    fun <PT> notify(property: KProperty<*>, oldValue: PT, newValue: PT) {
        notifyListeners()
    }

}