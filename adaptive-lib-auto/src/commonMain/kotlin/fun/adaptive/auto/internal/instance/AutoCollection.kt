package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class AutoCollection<BE : AutoCollectionBackend<IT>, PT : AutoCollectionPersistence<IT>, IT : AdatClass>(
    origin: Boolean,
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, PT, Collection<IT>, IT>(
    origin, defaultWireFormat, wireFormatProvider, scope
), Collection<IT> {

    override val size: Int
        get() = value.size

    val valueOrNull : Collection<IT>?
        get() = getItems() // FIXME valueOrNull in AutoCollection

    val value: Collection<IT>
        get() = getItems()

    override fun contains(element: IT): Boolean =
        value.contains(element)

    override fun containsAll(elements: Collection<IT>): Boolean =
        value.containsAll(elements)

    override fun isEmpty(): Boolean =
        value.isEmpty()

    override fun iterator(): Iterator<IT> =
        value.iterator()

    operator fun plusAssign(element: IT) {
        localAdd(element)
    }

    fun add(element: IT) {
        localAdd(element)
    }

    operator fun minusAssign(element : IT) {
        localRemove(itemId(element))
    }

    fun remove(element : IT) {
        localRemove(itemId(element))
    }

    fun remove(selector: (IT) -> Boolean) {
        getItems().filter(selector).forEach {
            localRemove(itemId(it))
        }
    }

    override fun persistenceInit() {
        persistence.save(backend.export())
    }

    override fun persistenceUpdate(itemId: ItemId, value: IT) {
        TODO("Not yet implemented")
    }

}