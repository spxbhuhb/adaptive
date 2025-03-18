package `fun`.adaptive.iot.space.model

import `fun`.adaptive.adaptive_lib_iot.generated.resources.area
import `fun`.adaptive.adaptive_lib_iot.generated.resources.building
import `fun`.adaptive.adaptive_lib_iot.generated.resources.floor
import `fun`.adaptive.adaptive_lib_iot.generated.resources.room
import `fun`.adaptive.adaptive_lib_iot.generated.resources.site
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class AioSpaceType {
    Site,
    Building,
    Floor,
    Room,
    Area;

    fun localized(): String =
        when (this) {
            Site -> Strings.site
            Building -> Strings.building
            Floor -> Strings.floor
            Room -> Strings.room
            Area -> Strings.area
        }

    companion object : EnumWireFormat<AioSpaceType>(entries) {
        override val wireFormatName: String
            get() = AioSpaceType.typeSignature().trimSignature()
    }
}