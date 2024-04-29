package hu.simplexion.z2.adaptive

interface AdaptiveTransformInterface {

    fun getThisClosureVariable(variableIndex : Int) : Any?

    fun setStateVariable(index: Int, value: Any?)

}