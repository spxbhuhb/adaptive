package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.model.NamedItemType
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

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

        inline fun <reified T> AvItem<*>.withSpec(): AvItem<T> {
            check(spec is T)
            @Suppress("UNCHECKED_CAST")
            return this as AvItem<T>
        }

    }

}