package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.model.AutoHandle

abstract class AutoItemFrontend<IT : AdatClass> : AutoFrontend<IT>() {

    abstract val value: IT

    override fun loadHandle(): AutoHandle? {
        return AutoHandle(collection = false)
    }

}