/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.foundation.testing.Traceable
import `fun`.adaptive.utility.alsoIfInstance
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder

val traceAll = Trace(".*")

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
            get() = "fun.adaptive.foundation.instruction.Trace"

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