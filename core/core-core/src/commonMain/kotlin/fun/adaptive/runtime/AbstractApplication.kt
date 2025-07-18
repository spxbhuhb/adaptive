package `fun`.adaptive.runtime

import `fun`.adaptive.resource.string.StringStoreResourceSet
import `fun`.adaptive.utility.Url
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.utility.firstInstanceOrNull

abstract class AbstractApplication<FW : AbstractWorkspace, BW : AbstractWorkspace> {

    abstract val version : String

    val modules = mutableSetOf<AppModule<FW,BW>>()

    abstract val backendWorkspace: BW
    abstract val frontendWorkspace: FW

    val stringStores = mutableListOf<StringStoreResourceSet>()
    val graphicsStores = mutableListOf<StringStoreResourceSet>()

    inline fun <reified T> firstContext() {
        backendWorkspace.contexts.firstInstanceOrNull<T>() ?: frontendWorkspace.contexts.firstInstance<T>()
    }

    inline fun <reified T> firstModule() = modules.firstInstance<T>()

    /**
     * Set the URL that belongs to the current state of the application. This is an optional
     * feature, applications may or may not implement it. The default implementation is an empty
     * function.
     */
    open fun setNavState(url: Url) {

    }

    /**
     * A callback that is called when the application navigation state changes.
     */
    open fun onNavStateChange(callback : (url : Url) -> Unit) {

    }
}