package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.internal.backend.AutoItemBackend
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.utility.ThreadSafe
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.WireFormatProvider
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.time.Duration

class AutoItem<BE : AutoItemBackend<IT>, PT : AutoItemPersistence<IT>, IT : AdatClass>(
    origin: Boolean,
    defaultWireFormat: AdatClassWireFormat<*>?,
    wireFormatProvider: WireFormatProvider,
    scope: CoroutineScope
) : AutoInstance<BE, PT, IT, IT>(
    origin, defaultWireFormat, wireFormatProvider, scope
) {

    val value: IT
        get() = getItem()

    val valueOrNull : IT?
        get() = getItemOrNull()

    fun <V> update(vararg changes: Pair<KProperty<V>, V>) {
        localUpdate(null, changes.map { it.first.name to it.second })
    }

    fun <V> update(vararg changes: KProperty0<V>) {
        localUpdate(null, changes.map { it.name to it.get() })
    }

    fun update(new: IT) {
        localUpdate(
            backend.itemId,
            new.adatCompanion.adatDescriptors.map {
                it.property.name to new.getValue(it.property.index)
            }
        )
    }

    override fun persistenceInit() {
        persistence?.save(backend.export(true))
    }

    override fun persistenceAdd(itemId: ItemId) {
        throw UnsupportedOperationException("item instances does not support add")
    }

    override fun persistenceUpdate(itemId: ItemId) {
        persistence?.save(backend.export(true))
    }

    override fun persistenceRemove(itemId: ItemId, value: IT?) {
        throw UnsupportedOperationException("item instances does not support remove")

    }

    @ThreadSafe
    suspend fun ensureValue(timeout : Duration = Duration.INFINITE) : AutoItem<BE,PT,IT> {
        waitFor(timeout) { isInitialized }
        return this
    }

}