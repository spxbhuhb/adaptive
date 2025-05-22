package `fun`.adaptive.adat.visitor

import `fun`.adaptive.adat.AdatClass

abstract class AdatClassTransformerVoid : AdatClassTransformer<Nothing?>() {

    open fun visitInstance(instance: AdatClass): AdatClass? {
        return instance.transformChildren(this, null)
    }

    final override fun visitInstance(instance: AdatClass, data: Nothing?): AdatClass? =
        visitInstance(instance)

}