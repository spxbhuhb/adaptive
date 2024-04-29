/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.utility.FqNameAware

interface WireFormat<T> : FqNameAware {

    override val fqName: String
        get() = wireFormatCompanion.fqName

    val kind: WireFormatKind
        get() = WireFormatKind.Instance

    val wireFormatCompanion: WireFormat<T>
        get() {
            throw UnsupportedOperationException("This code should be replaced by the Adaptive plugin for classes and never be called for companions and objects.")
        }

    fun wireFormatEncode(encoder: WireFormatEncoder, value: T): WireFormatEncoder =
        wireFormatCompanion.wireFormatEncode(encoder, value)

    fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): T =
        wireFormatCompanion.wireFormatDecode(source, decoder)

}