package `fun`.adaptive.foundation.value


class AdaptiveValueStore<VT>(
    value: VT
) {

    var value: VT = value
        set(v) {
            field = v
            notifyListeners()
        }

    val listeners = mutableListOf<AdaptiveValueListener<VT>>()

    fun notifyListeners() {
        for (listener in listeners) listener.onValueChanged(value)
    }

    fun addListener(listener: AdaptiveValueListener<VT>) {
        listeners += listener
    }

    fun removeListener(listener: AdaptiveValueListener<VT>) {
        listeners -= listener
    }

}