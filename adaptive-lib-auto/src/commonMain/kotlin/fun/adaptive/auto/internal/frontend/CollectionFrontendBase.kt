package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.auto.model.ItemId

abstract class CollectionFrontendBase<A> : FrontendBase() {

    abstract var values : List<A>
        protected set

    abstract fun commit(itemId: ItemId, newValue : A, oldValue : A?, initial: Boolean, fromBackend: Boolean)

    abstract fun add(item: A)

    abstract fun remove(itemId: ItemId)

    abstract fun remove(selector: (A) -> Boolean)

}