package `fun`.adaptive.adat.visitor

import `fun`.adaptive.adat.AdatClass

abstract class AdatClassTransformer<in D> : AdatClassVisitor<AdatClass?, D>() {

    override fun visitInstance(instance: AdatClass, data: D): AdatClass? {
        return instance.transformChildren(this, data)
    }

}