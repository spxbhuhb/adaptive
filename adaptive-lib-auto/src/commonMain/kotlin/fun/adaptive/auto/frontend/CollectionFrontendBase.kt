package `fun`.adaptive.auto.frontend

import `fun`.adaptive.auto.ItemId

abstract class CollectionFrontendBase : FrontendBase() {

    abstract fun commit(itemId: ItemId)

}