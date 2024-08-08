package hu.simplexion.adaptive.auto.frontend

import hu.simplexion.adaptive.auto.ItemId

abstract class CollectionFrontendBase : FrontendBase() {

    abstract fun commit(itemId: ItemId)

}