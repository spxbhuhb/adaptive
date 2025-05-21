package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat
import kotlin.enums.EnumEntries

enum class SplitVisibility {
    None,
    First,
    Second,
    Both;

    companion object : EnumWireFormat<SplitVisibility>(entries) {
        override val wireFormatName: String
            get() = SplitVisibility.typeSignature().trimSignature()
    }
}