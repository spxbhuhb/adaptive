package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.ThreadSafe
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.time.Duration

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

    val valueOrNull: Collection<IT>?
        get() = getItemsOrNull()

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

    fun <V> update(vararg changes: Pair<KProperty<V>, V>, selector: (IT) -> Boolean) {
        val changes = changes.map { it.first.name to it.second }
        value.filter(selector).forEach {
            localUpdate(itemId(it), changes)
        }
    }

    fun <V> update(vararg changes: KProperty1<IT, V>, selector: (IT) -> Boolean) {
        val changes = changes.map { it.name to it.get(value.first()) }
        value.filter(selector).forEach {
            localUpdate(itemId(it), changes)
        }
    }

    operator fun minusAssign(element: IT) {
        localRemove(itemId(element))
    }

    fun remove(element: IT) {
        localRemove(itemId(element))
    }

    fun remove(selector: (IT) -> Boolean) {
        value.filter(selector).forEach {
            localRemove(itemId(it))
        }
    }

    override fun persistenceInit() {
        persistence?.update(backend.export(withItems = true))
    }

    override fun persistenceAdd(itemId: ItemId) {
        persistence?.add(backend.exportItem(itemId) !!)
    }

    override fun persistenceUpdate(itemId: ItemId) {
        persistence?.update(backend.exportItem(itemId) !!)
    }

    override fun persistenceRemove(itemId: ItemId, value: IT?) {
        persistence?.remove(itemId, value)
    }

    @ThreadSafe
    suspend fun ensureValue(timeout : Duration = Duration.INFINITE) : AutoCollection<BE,PT,IT> {
        waitFor(timeout) { isInitialized }
        return this
    }

}