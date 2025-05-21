package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class Orientation {
    Horizontal,
    Vertical;

    companion object : EnumWireFormat<Orientation>(Orientation.Companion.entries) {
        override val wireFormatName: String
            get() = Orientation.typeSignature().trimSignature()
    }
}