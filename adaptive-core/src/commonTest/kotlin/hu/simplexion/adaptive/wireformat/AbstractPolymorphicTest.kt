/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat
import hu.simplexion.adaptive.wireformat.builtin.PolymorphicWireFormat
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class AbstractPolymorphicTest<ST>(
    wireFormatProvider: WireFormatProvider
) : AbstractWireFormatTest<ST>(wireFormatProvider) {

    @Test
    fun basic() {
        WireFormatRegistry += C1.Companion
        WireFormatRegistry += C2.Companion

        C1(12).also { assertEquals(it, polymorphicActual(it, C1.Companion)) }
        C2(23).also { assertEquals(it, polymorphicActual(it, C2.Companion)) }
    }

    @Test
    fun list() {
        WireFormatRegistry += C1.Companion
        WireFormatRegistry += C2.Companion

        listOf(C1(12), C2(23)).also { assertEquals(it, polymorphicActual(it, ListWireFormat(WireFormatTypeArgument<CI>(PolymorphicWireFormat(), false)))) }
    }

    interface CI {
        var i: Int
    }

    data class C1(
        override var i: Int = 0
    ) : AdatClass<C1>, CI {

        override val adatCompanion: AdatCompanion<C1>
            get() = Companion

        override fun genGetValue(index: Int): Any {
            return i
        }

        override fun genSetValue(index: Int, value: Any?) {
            i = value as Int
        }

        companion object : AdatCompanion<C1> {

            override val wireFormatName: String
                get() = "hu.simplexion.adaptive.wireformat.C1"

            override val adatMetadata = AdatClassMetadata<C1>(
                version = 1,
                name = "hu.simplexion.adaptive.wireformat.C1",
                flags = 0,
                properties = listOf(
                    AdatPropertyMetadata("i", 0, 0, "I"),
                )
            )

            override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

            override fun wireFormatEncode(encoder: WireFormatEncoder, value: C1) =
                encoder.int(1, "i", value.i)

            override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): C1 =
                if (decoder == null) C1(0) else C1(decoder.int(1, "i"))
        }
    }

    data class C2(
        override var i: Int = 0
    ) : AdatClass<C2>, CI {

        override val adatCompanion: AdatCompanion<C2>
            get() = Companion

        override fun genGetValue(index: Int): Any {
            return i
        }

        override fun genSetValue(index: Int, value: Any?) {
            i = value as Int
        }

        companion object : AdatCompanion<C2> {

            override val wireFormatName: String
                get() = "hu.simplexion.adaptive.wireformat.C2"

            override val adatMetadata = AdatClassMetadata<C2>(
                version = 1,
                name = "hu.simplexion.adaptive.wireformat.C2",
                flags = 0,
                properties = listOf(
                    AdatPropertyMetadata("i", 0, 0, "I"),
                )
            )

            override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

            override fun wireFormatEncode(encoder: WireFormatEncoder, value: C2) =
                encoder.int(1, "i", value.i)

            override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): C2 =
                if (decoder == null) C2(0) else C2(decoder.int(1, "i"))
        }
    }
}