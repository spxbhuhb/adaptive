package `fun`.adaptive.sandbox.support

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class E {
    V1, V2, V3, V4, V5;

    companion object : EnumWireFormat<E>(entries) {
        override val wireFormatName: String
            get() = E.typeSignature().trimSignature()
    }
}