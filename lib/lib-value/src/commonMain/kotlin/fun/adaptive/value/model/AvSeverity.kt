package `fun`.adaptive.value.model

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class AvSeverity {
    Success, Info, Warning, Error;

    companion object : EnumWireFormat<AvSeverity>(entries) {
        override val wireFormatName: String
            get() = AvSeverity.typeSignature().trimSignature()
    }
}