/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

data class A(
    var b: Boolean = false,
    var i: Int = 0,
    var s: String = "",
    var l: MutableList<Int> = mutableListOf()
) {
    companion object : WireFormat<A> {

        override val wireFormatName: String
            get() = "fun.adaptive.wireformat.A"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: A) =
            encoder
                .boolean(1, "b", value.b)
                .int(2, "i", value.i)
                .string(3, "s", value.s)
                .intArray(4, "l", value.l.toIntArray())

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): A {
            if (decoder == null) return A()

            return A(
                decoder.boolean(1, "b"),
                decoder.int(2, "i"),
                decoder.string(3, "s"),
                decoder.intArray(4, "l").toMutableList()
            )
        }
    }
}

data class B(
    var a: A = A(),
    var s: String = ""
) {
    companion object : WireFormat<B> {

        override val wireFormatName: String
            get() = "fun.adaptive.wireformat.B"

        override fun wireFormatEncode(encoder: WireFormatEncoder, value: B) =
            encoder
                .instance(1, "a", value.a, A)
                .string(2, "s", value.s)

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): B {
            if (decoder == null) return B()

            return B(
                decoder.instance(1, "a", A),
                decoder.string(2, "s")
            )
        }
    }
}

enum class E {
    V1,
    V2
}