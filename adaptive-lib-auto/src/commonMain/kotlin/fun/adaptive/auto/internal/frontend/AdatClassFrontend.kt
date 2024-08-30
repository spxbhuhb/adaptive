package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.deepCopy
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.model.ItemId

class AdatClassFrontend<A : AdatClass<A>>(
    override val backend: PropertyBackend,
    val companion: AdatCompanion<A>,
    initialValue: A?,
    val itemId : ItemId?,
    val collectionFrontend: CollectionFrontendBase?,
    val onCommit: ((frontend: AdatClassFrontend<A>) -> Unit)? = null
) : FrontendBase() {

    val adatContext = AdatContext<ItemId>(itemId, null, null, store = this, null)

    var value: A? = initialValue?.deepCopy()?.also {
        @Suppress("UNCHECKED_CAST")
        it.adatContext = adatContext as AdatContext<Any>
    }

    override fun commit() {
        val newValue = companion.newInstance(backend.values)

        @Suppress("UNCHECKED_CAST")
        newValue.adatContext = adatContext as AdatContext<Any>
        newValue.validate()

        value = newValue
        collectionFrontend?.commit(backend.itemId)
        onCommit?.invoke(this)
    }

    fun modify(propertyName: String, propertyValue: Any?) {
        backend.modify(backend.itemId, propertyName, propertyValue)
    }

    override fun update(instance: AdatClass<*>, path: Array<String>, value: Any?) {
        // FIXME only single properties are handled b y AdatClassListFrontend
        check(path.size == 1) { "multi-level paths are not implemented yet" }
        modify(path[0], value)
    }


}