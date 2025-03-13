package `fun`.adaptive.iot.model.space

import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class AioSpaceType {
    Site,
    Building,
    Floor,
    Room,
    Area;

    companion object : EnumWireFormat<AioSpaceType>(entries) {
        override val wireFormatName: String
            get() = "fun.adaptive.iot.model.space.AioSpaceType"
    }
}