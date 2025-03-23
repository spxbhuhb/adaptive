package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
data class AvItem<T>(
    override val name: String,
    override val type: WsItemType,
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AvMarkerMap?,
    val parentId: AvValueId?,
    val specific: T?
) : AvValue() {

    val markers: AvMarkerMap
        get() = markersOrNull ?: emptyMap()

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
            if (specific == null) return this

            @Suppress("USELESS_IS_CHECK") // not useless in this case actually
            check(specific is T)

            return this
        }
    }

}