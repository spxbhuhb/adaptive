package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.workspace.model.WsItemType
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Instant

@Adat
data class AvItem(
    override val name: String,
    override val type: WsItemType,
    override val uuid: AvValueId,
    override val timestamp: Instant,
    override val status: AvStatus,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AvMarkerMap?,
    val parentId: AvValueId?
) : AvValue() {

    val markers: AvMarkerMap
        get() = markersOrNull ?: emptyMap()

    operator fun contains(markerName: AvMarker): Boolean {
        return markersOrNull?.containsKey(markerName) == true
    }

    operator fun get(markerName: AvMarker): AvValueId? {
        return markersOrNull?.get(markerName)
    }

    fun copyWith(value: AvMarkerValue): AvItem {
        return copyWith(value.markerName, value)
    }

    fun copyWith(key: AvMarker, value: AvMarkerValue?): AvItem {
        val markers = markersOrNull?.toMutableMap() ?: mutableMapOf()
        markers[key] = value?.uuid
        return copy(markersOrNull = markers)
    }


//    fun icon(): GraphicsResourceSet {
//        if (markersOrNull == null) return Graphics.empty
//        for (marker in markersOrNull.keys) {
//            val icon = iconCache[marker]
//            if (icon != null) return icon
//        }
//        return Graphics.empty
//    }



}