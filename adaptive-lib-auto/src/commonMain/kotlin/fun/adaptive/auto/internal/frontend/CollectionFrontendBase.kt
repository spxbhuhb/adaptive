package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.auto.model.ItemId

abstract class CollectionFrontendBase : FrontendBase() {

    abstract fun commit(itemId: ItemId)

}