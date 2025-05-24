package `fun`.adaptive.example

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class ExampleEnum {
    V1,
    V2;

    // you also need to register the enum in the `wireFormatInit` function of the application module
    companion object : EnumWireFormat<ExampleEnum>(entries) {
        override val wireFormatName: String
            get() = ExampleEnum.typeSignature().trimSignature()
    }
}