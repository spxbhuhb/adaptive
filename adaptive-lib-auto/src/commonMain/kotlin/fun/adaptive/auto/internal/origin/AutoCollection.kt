package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.frontend.AutoCollectionFrontend
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope

class AutoCollection<BE : AutoBackend<IT>, FE : AutoCollectionFrontend<IT>, IT : AdatClass>(
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, FE, Collection<IT>, IT>(
    defaultWireFormat, wireFormatProvider, scope
), Collection<IT> {

    override fun commit(itemBackend: AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean) {
        frontend.commit(itemBackend, initial, fromPeer)
    }

    override val size: Int
        get() = values.size

    val values: Collection<IT>
        get() = frontend.values

    override fun contains(element: IT): Boolean =
        values.contains(element)

    override fun containsAll(elements: Collection<IT>): Boolean =
        values.containsAll(elements)

    override fun isEmpty(): Boolean =
        values.isEmpty()

    override fun iterator(): Iterator<IT> =
        values.iterator()

    operator fun plusAssign(element: IT) {
        frontend.add(element)
    }

    fun add(element: IT) {
        frontend.add(element)
    }

    fun remove(selector: (IT) -> Boolean) {
        frontend.remove(selector)
    }

}