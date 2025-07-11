package `fun`.adaptive.sandbox.support

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class ExampleEnum {
    V1, V2, V3, V4, V5;

    companion object : EnumWireFormat<ExampleEnum>(entries) {
        override val wireFormatName: String
            get() = ExampleEnum.typeSignature().trimSignature()
    }

}