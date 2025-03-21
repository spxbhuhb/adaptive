package `fun`.adaptive.iot.space.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.resource.string.Strings

val AvItem.localizedSpaceType: String
    get() = when (this.type.substringAfterLast(':')) {
        "site" -> Strings.site
        "building" -> Strings.building
        "floor" -> Strings.floor
        "room" -> Strings.room
        "area" -> Strings.area
        else -> Strings.noname
    }