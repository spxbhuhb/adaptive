package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.value.AvValue

val AvValue<AioSpaceSpec>.localizedSpaceType: String
    get() = when (this.type.substringAfterLast(':')) {
        SpaceMarkers.ROOM -> Strings.room
        SpaceMarkers.AREA -> Strings.area
        SpaceMarkers.FLOOR -> Strings.floor
        SpaceMarkers.BUILDING -> Strings.building
        SpaceMarkers.SITE -> Strings.site
        else -> Strings.space
    }