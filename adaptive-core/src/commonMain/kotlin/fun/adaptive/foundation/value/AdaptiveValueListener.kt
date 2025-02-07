package `fun`.adaptive.foundation.value

interface AdaptiveValueListener<VT> {
    fun onValueChanged(value: VT)
}