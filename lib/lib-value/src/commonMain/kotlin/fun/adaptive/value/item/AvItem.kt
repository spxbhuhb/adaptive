package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

@Adat
data class AvItem<T>(
    override val name: String,
    override val type: NamedItemType,
    override val uuid: AvValueId = uuid7(),
    override val timestamp: Instant = now(),
    override val status: AvStatus = AvStatus.OK,
    override val parentId: AvValueId? = null,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AvMarkerMap? = null,
    val spec: T
) : AvValue() {

    val markers: AvMarkerMap
        get() = markersOrNull ?: emptyMap()

    fun toMutableMarkers() =
        markersOrNull?.toMutableMap() ?: mutableMapOf()

    operator fun contains(markerName: AvMarker): Boolean {
        return markersOrNull?.containsKey(markerName) == true
    }

    operator fun get(markerName: AvMarker): AvValueId? {
        return markersOrNull?.get(markerName)
    }

    fun copyWith(value: AvMarkerValue): AvItem<T> {
        return copyWith(value.markerName, value)
    }

    fun copyWith(key: AvMarker, value: AvMarkerValue?): AvItem<T> {
        val markers = markersOrNull?.toMutableMap() ?: mutableMapOf()
        markers[key] = value?.uuid
        return copy(markersOrNull = markers)
    }

    override fun toString(): String {
        return name
    }

    companion object {
        inline fun <reified T> Any.asAvItem(): AvItem<T> {

            @Suppress("UNCHECKED_CAST") // just checked
            this as AvItem<T>

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            check(spec is T)

            return this
        }

        inline fun <reified T> Any.asAvItemOrNull(): AvItem<T>? {

            @Suppress("UNCHECKED_CAST") // just checked
            this as AvItem<T>

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            if (spec !is T) return null

            return this
        }

        inline fun <reified T : Any> AvValue.withSpec(): AvItem<T> =
            withSpec(T::class)

        fun <T : Any> AvValue.withSpec(kClass : KClass<T>): AvItem<T> {
            this as AvItem<*>
            check(kClass.isInstance(spec)) { "Spec is not of type $kClass for item $this" }
            @Suppress("UNCHECKED_CAST")
            return this as AvItem<T>
        }

        inline fun <reified T : Any> AvItem<*>.withSpec(): AvItem<T> =
            withSpec(T::class)

        fun <T : Any> AvItem<*>.withSpec(kClass : KClass<T>): AvItem<T> {
            check(kClass.isInstance(spec)) { "Spec is not of type $kClass for item $this" }
            @Suppress("UNCHECKED_CAST")
            return this as AvItem<T>
        }
    }

}