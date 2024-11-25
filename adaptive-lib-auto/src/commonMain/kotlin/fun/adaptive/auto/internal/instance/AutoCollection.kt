package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class AutoCollection<BE : AutoBackend<IT>, PT : AutoCollectionPersistence<IT>, IT : AdatClass>(
    origin: Boolean,
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, PT, Collection<IT>, IT>(
    origin, defaultWireFormat, wireFormatProvider, scope
), Collection<IT> {

    override val size: Int
        get() = values.size

    val values: Collection<IT>
        get() = getItems()

    override fun contains(element: IT): Boolean =
        values.contains(element)

    override fun containsAll(elements: Collection<IT>): Boolean =
        values.containsAll(elements)

    override fun isEmpty(): Boolean =
        values.isEmpty()

    override fun iterator(): Iterator<IT> =
        values.iterator()

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

}