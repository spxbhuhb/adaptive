package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class SnackType {
    Success,
    Info,
    Warning,
    Fail;

    companion object : EnumWireFormat<SnackType>(entries) {
        override val wireFormatName: String
            get() = SnackType.typeSignature().trimSignature()
    }
}