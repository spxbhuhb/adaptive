/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder

fun trace(vararg patterns: String) = Trace(*patterns.map { Regex(it) }.toTypedArray())

/**
 * Set trace for the given fragment.
 *
 * @patterns The point names to look for. Default is everything, but you can easily
 *           specify a single point like: `Trace(Regex("layout"))`
 */
class Trace(vararg val patterns : Regex = arrayOf(Regex(".*"))) : AdaptiveInstruction {

    constructor(vararg patterns: String) : this(*patterns.map { Regex(it) }.toTypedArray())

    override fun apply(subject: Any) {
        subject.alsoIfInstance<Traceable> { it.tracePatterns = patterns }
    }

    companion object : WireFormat<Trace> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.foundation.instruction.Trace"

        // FIXME trace wireformat
        override fun wireFormatEncode(encoder: WireFormatEncoder, value: Trace): WireFormatEncoder {
            return encoder
        }

        override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Trace {
            check(decoder != null)
            return Trace()
        }

    }
}