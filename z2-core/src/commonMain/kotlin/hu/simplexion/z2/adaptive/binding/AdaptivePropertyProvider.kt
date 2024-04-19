package hu.simplexion.z2.adaptive.binding

interface AdaptivePropertyProvider {

    fun addBinding(binding: AdaptiveStateVariableBinding<*>)

    fun removeBinding(binding: AdaptiveStateVariableBinding<*>)

    fun getValue(path : Array<String>) : Any?

    fun setValue(path : Array<String>, value : Any?, fromBinding: AdaptiveStateVariableBinding<*>)

}