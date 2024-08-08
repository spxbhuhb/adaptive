package hu.simplexion.adaptive.auto.frontend

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.AdatContext
import hu.simplexion.adaptive.adat.deepCopy
import hu.simplexion.adaptive.adat.store.AdatStore
import hu.simplexion.adaptive.auto.backend.PropertyBackend

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