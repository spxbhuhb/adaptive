package `fun`.adaptive.adat.nocode

import `fun`.adaptive.adat.AdatClass

class AdatClassIntersection(
    override val adatCompanion: NoCodeAdatCompanion,
    val instances : Collection<AdatClass>,
    val setValueFun : (index : Int, value : Any?) -> Unit,
) : AdatClass {

    override fun genGetValue(index: Int): Any? {
        var intersectedValue : Any? = null

        for (instance in instances) {
            val instanceValue = instance.genGetValue(index)

            if (instanceValue != intersectedValue) {
                if (intersectedValue != null) return null
                intersectedValue = instanceValue
            }
        }

        return intersectedValue
    }

    override fun genSetValue(index: Int, value: Any?) {
        setValueFun(index, value)
    }

}