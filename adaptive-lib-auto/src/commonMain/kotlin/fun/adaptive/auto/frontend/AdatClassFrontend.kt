package `fun`.adaptive.auto.frontend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.deepCopy
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.backend.PropertyBackend

class AdatClassFrontend<A : AdatClass<A>>(
    val backend: PropertyBackend,
    val companion: AdatCompanion<A>,
    initialValue: A?,
    val collectionFrontend: CollectionFrontendBase?
) : FrontendBase(), AdatStore {

    val adatContext = AdatContext(null, null, store = this, null)

    var value: A? = initialValue?.deepCopy()?.also {
        it.adatContext = adatContext
    }

    override fun commit() {
        val newValue = companion.newInstance(backend.values)
        newValue.adatContext = adatContext
        value = newValue
        collectionFrontend?.commit(backend.itemId)
    }

    fun modify(propertyName: String, propertyValue: Any?) {
        backend.modify(backend.itemId, propertyName, propertyValue)
    }

}