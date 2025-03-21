package `fun`.adaptive.iot.item

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.ui.iconCache
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.builtin.empty
import `fun`.adaptive.ui.workspace.model.WsItemType
import kotlinx.datetime.Instant

@Adat
data class AioItem(
    override val name: String,
    override val type: WsItemType,
    override val uuid: AioValueId,
    override val timestamp: Instant,
    override val status: AioStatus,
    val friendlyId: FriendlyItemId,
    val markersOrNull: AioMarkerMap?,
    val parentId: AioValueId?
) : AioValue() {

    val markers: AioMarkerMap
        get() = markersOrNull ?: emptyMap()

    operator fun contains(markerName : AioMarker) : Boolean {
        return markersOrNull?.containsKey(markerName) == true
    }

    operator fun get(markerName : AioMarker) : AioValueId? {
        return markersOrNull?.get(markerName)
    }

    fun copyWith(value: AioMarkerValue): AioItem {
        return copyWith(value.markerName, value)
    }

    fun copyWith(key: AioMarker, value: AioMarkerValue?): AioItem {
        val markers = markersOrNull?.toMutableMap() ?: mutableMapOf()
        markers[key] = value?.uuid
        return copy(markersOrNull = markers)
    }

    fun icon(): GraphicsResourceSet {
        if (markersOrNull == null) return Graphics.empty
        for (marker in markersOrNull.keys) {
            val icon = iconCache[marker]
            if (icon != null) return icon
        }
        return Graphics.empty
    }

}