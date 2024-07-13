/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatRegistry
import hu.simplexion.adaptive.wireformat.builtin.*
import hu.simplexion.adaptive.wireformat.signature.WireFormatType
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument
import hu.simplexion.adaptive.wireformat.signature.parseTypeSignature

fun AdatPropertyMetadata.toPropertyWireFormat(): AdatPropertyWireFormat<*> =
    AdatPropertyWireFormat(this, parseTypeSignature(signature).toWireFormat())

internal fun WireFormatType.toWireFormat(): WireFormat<*> =
    when {
        generics.isNotEmpty() -> {
            val args = generics.map { WireFormatTypeArgument(it.toWireFormat(), it.nullable) }
            val genericFormat = checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
            genericFormat.wireFormatCopy(args)
        }

        name.length > 3 -> {
            checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
        }

        else -> {
            val length = name.length

            when (length) {
                1 -> when(name) {
                    "T" -> StringWireFormat
                    "I" -> IntWireFormat
                    "Z" -> BooleanWireFormat
                    "U" -> UuidWireFormat
                    "J" -> LongWireFormat

                    "D" -> DoubleWireFormat
                    "F" -> FloatWireFormat

                    "C" -> CharWireFormat

                    "S" -> ShortWireFormat
                    "B" -> ByteWireFormat
                    "0" -> UnitWireFormat

                    "*" -> PolymorphicWireFormat<Any>()

                    else -> checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
                }
                2 -> when(name) {
                    "[Z" -> BooleanArrayWireFormat
                    "[I" -> IntArrayWireFormat
                    "[S" -> ShortArrayWireFormat
                    "[B" -> ByteArrayWireFormat
                    "[F" -> FloatArrayWireFormat
                    "[D" -> DoubleArrayWireFormat
                    "[C" -> CharArrayWireFormat
                    "+I" -> UIntWireFormat
                    "+S" -> UShortWireFormat
                    "+B" -> UByteWireFormat
                    "+J" -> ULongArrayWireFormat
                    else -> checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
                }
                else -> when (name) {
                    "[+I" -> UIntArrayWireFormat
                    "[+S" -> UShortArrayWireFormat
                    "[+B" -> UByteArrayWireFormat
                    "[+J" -> ULongArrayWireFormat
                    else -> checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
                }
            }
        }
    }