/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.wireformat.WireFormatRegistry

val nop = NOP()

/**
 * A no-operation instruction, ignored.
 */
@Adat
class NOP : AdatClass, AdaptiveInstruction {

    override val adatCompanion = Companion

    override var adatContext: AdatContext<*>? = null

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? NOP)

    override fun hashCode(): Int =
        adatHashCode()

    override fun toString(): String =
        "NoOp()"

    override fun genGetValue(index: Int): Any? {
        invalidIndex(index)
    }

    override fun genSetValue(index: Int, value: Any?) {
        invalidIndex(index)
    }

    companion object : AdatCompanion<NOP> {

        override val wireFormatName: String
            get() = "fun.adaptive.foundation.instruction.NoOp"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.foundation.instruction.NoOp",
            flags = 0,
            properties = emptyList()
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors = adatMetadata.generateDescriptors()

        @Suppress("UNCHECKED_CAST")
        override fun newInstance(values: Array<Any?>): NOP =
            NOP()

        init {
            WireFormatRegistry += adatWireFormat
        }
    }

}