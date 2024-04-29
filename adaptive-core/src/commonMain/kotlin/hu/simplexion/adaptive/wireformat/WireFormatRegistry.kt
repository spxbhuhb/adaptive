/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

class WireFormatRegistry {

    val formats = mutableMapOf<String, WireFormat<*>>()

    fun <ST> decodeInstance(source: ST, wireFormatDecoder: WireFormatDecoder<ST>, type: String): Any? =
        checkNotNull(formats[type]) { "missing wire format for $type" }.wireFormatDecode(source, wireFormatDecoder)

}