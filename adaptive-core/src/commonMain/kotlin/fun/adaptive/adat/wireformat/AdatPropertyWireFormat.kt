/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.wireformat

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import kotlin.collections.plus

class AdatPropertyWireFormat<T>(
    val metadata: AdatPropertyMetadata,
    val wireFormat: WireFormat<T>
) {

    val index: Int
        get() = metadata.index

    val fieldNumber: Int
        inline get() = metadata.index + 1

    val name: String
        get() = metadata.name

    fun encode(encoder: WireFormatEncoder, instance: AdatClass) {
        withErrorReport {
            @Suppress("UNCHECKED_CAST")
            wireFormat.wireFormatEncode(encoder, fieldNumber, metadata.name, instance.getValue(metadata.index) as T?)
        }
    }

    fun encode(encoder: WireFormatEncoder, values: Array<Any?>) {
        withErrorReport {
            @Suppress("UNCHECKED_CAST")
            wireFormat.wireFormatEncode(encoder, fieldNumber, metadata.name, values[metadata.index] as T?)
        }
    }

    fun decode(decoder: WireFormatDecoder<*>, values: Array<Any?>) {
        withErrorReport {
            val value = wireFormat.wireFormatDecode(decoder, fieldNumber, metadata.name)
            values[metadata.index] = if (metadata.isNullable || metadata.hasDefault) value else checkNotNull(value)
        }
    }

    inline fun <T> withErrorReport(block: () -> T): T =
        try {
            block()
        } catch (ex: Exception) {
            if (ex is AdatWireFormatError) {
                throw AdatWireFormatError(ex.formats + this, ex.cause)
            } else {
                throw AdatWireFormatError(listOf(this), ex)
            }
        }
}