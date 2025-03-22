package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.iot.space.marker.SpaceMarkers
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.resource.string.Strings

val AvItem.localizedSpaceType: String
    get() = when (this.type.substringAfterLast(':')) {
        SpaceMarkers.ROOM -> Strings.room
        SpaceMarkers.AREA -> Strings.area
        SpaceMarkers.FLOOR -> Strings.floor
        SpaceMarkers.BUILDING -> Strings.building
        SpaceMarkers.SITE -> Strings.site
        else -> Strings.space
    }