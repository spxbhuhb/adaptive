package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.frontend.CollectionFrontendBase
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.service.ServiceContext

class OriginListBase<BE : BackendBase, FE : CollectionFrontendBase<IT>, IT : AdatClass>(
    worker: AutoWorker?,
    handle: AutoHandle,
    serviceContext: ServiceContext?,
    defaultWireFormat: AdatClassWireFormat<*>?,
    trace: Boolean,
    register: Boolean = true,
    builder: OriginBase<BE, FE, List<IT>, IT>.() -> Unit
) : OriginBase<BE, FE, List<IT>, IT>(
    worker, handle, serviceContext, defaultWireFormat, trace, register, builder
), List<IT> {

    override val size: Int
        get() = values.size

    val values: List<IT>
        inline get() = frontend.values

    override fun contains(element: IT): Boolean =
        values.contains(element)

    override fun containsAll(elements: Collection<IT>): Boolean =
        values.containsAll(elements)

    override fun isEmpty(): Boolean =
        values.isEmpty()

    override fun iterator(): Iterator<IT> =
        values.iterator()

    override fun indexOf(element: IT): Int =
        values.indexOf(element)

    override fun lastIndexOf(element: IT): Int =
        values.lastIndexOf(element)

    override operator fun get(index: Int) =
        values[index]

    override fun listIterator(): ListIterator<IT> =
        values.listIterator()

    override fun listIterator(index: Int): ListIterator<IT> =
        values.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<IT> =
        values.subList(fromIndex, toIndex)

    fun add(element: IT) {
        frontend.add(element)
    }

    fun remove(selector: (IT) -> Boolean) {
        frontend.remove(selector)
    }

}