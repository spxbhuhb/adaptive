package `fun`.adaptive.runtime

import `fun`.adaptive.resource.string.StringStoreResourceSet
import `fun`.adaptive.utility.firstInstance
import `fun`.adaptive.utility.firstInstanceOrNull

abstract class AbstractApplication<FW : AbstractWorkspace, BW : AbstractWorkspace> {

    val modules = mutableSetOf<AppModule<FW,BW>>()

    abstract val backendWorkspace: BW
    abstract val frontendWorkspace: FW

    val stringStores = mutableListOf<StringStoreResourceSet>()
    val graphicsStores = mutableListOf<StringStoreResourceSet>()

    inline fun <reified T> firstContext() {
        backendWorkspace.contexts.firstInstanceOrNull<T>() ?: frontendWorkspace.contexts.firstInstance<T>()
    }

    inline fun <reified T> firstModule() = modules.firstInstance<T>()

}