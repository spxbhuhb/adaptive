package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.origin.AutoInstance

abstract class AutoItemFrontend<IT : AdatClass> : AutoFrontend<IT, IT>() {

    abstract val value: IT

    /**
     * @see AutoInstance.commit
     */
    abstract fun commit(itemBackend : AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean)

}