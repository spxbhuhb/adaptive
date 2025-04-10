package `fun`.adaptive.runtime

import `fun`.adaptive.resource.string.StringStoreResourceSet
import `fun`.adaptive.utility.firstInstance

abstract class AbstractApplication<WT : AbstractWorkspace> {

    val modules = mutableSetOf<AppModule<WT>>()

    abstract val workspace: WT

    val stringStores = mutableListOf<StringStoreResourceSet>()

    inline fun <reified T> firstContext() = workspace.contexts.firstInstance<T>()

}