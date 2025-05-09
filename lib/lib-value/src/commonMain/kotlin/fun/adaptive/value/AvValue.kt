package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.item.AvMarker
import `fun`.adaptive.value.item.AvMarkerMap
import `fun`.adaptive.value.item.AvMarkerValue
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.item.FriendlyItemId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

@Adat
data class AvValue<T>(
    override val name: String,
    override val type: NamedItemType,
    override val uuid: AvValueId = UUID.Companion.uuid7(),
    override val timestamp: Instant = Clock.System.now(),
    override val status: AvStatus = AvStatus.Companion.OK,
    override val parentId: AvValueId? = null,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AvMarkerMap? = null,
    val spec: T
) : AvValue2() {

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

    fun copyWith(value: AvMarkerValue): AvValue<T> {
        return copyWith(value.markerName, value)
    }

    fun copyWith(key: AvMarker, value: AvMarkerValue?): AvValue<T> {
        val markers = markersOrNull?.toMutableMap() ?: mutableMapOf()
        markers[key] = value?.uuid
        return copy(markersOrNull = markers)
    }

    override fun toString(): String {
        return name
    }

    companion object {
        inline fun <reified T> Any.asAvItem(): AvValue<T> {

            @Suppress("UNCHECKED_CAST") // just checked
            this as AvValue<T>

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            check(spec is T)

            return this
        }

        inline fun <reified T> Any.asAvItemOrNull(): AvValue<T>? {

            @Suppress("UNCHECKED_CAST") // just checked
            this as AvValue<T>

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            if (spec !is T) return null

            return this
        }

        inline fun <reified T : Any> AvValue2.withSpec(): AvValue<T> =
            withSpec(T::class)

        fun <T : Any> AvValue2.withSpec(kClass: KClass<T>): AvValue<T> {
            this as AvValue<*>
            check(kClass.isInstance(spec)) { "Spec is not of type $kClass for item $this" }
            @Suppress("UNCHECKED_CAST")
            return this as AvValue<T>
        }

        inline fun <reified T : Any> AvValue<*>.withSpec(): AvValue<T> =
            withSpec(T::class)

        fun <T : Any> AvValue<*>.withSpec(kClass: KClass<T>): AvValue<T> {
            check(kClass.isInstance(spec)) { "Spec is not of type $kClass for item $this" }
            @Suppress("UNCHECKED_CAST")
            return this as AvValue<T>
        }
    }

}