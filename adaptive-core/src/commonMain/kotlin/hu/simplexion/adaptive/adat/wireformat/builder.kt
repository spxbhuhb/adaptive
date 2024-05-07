/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatRegistry
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument
import hu.simplexion.adaptive.wireformat.builtin.BooleanWireFormat
import hu.simplexion.adaptive.wireformat.builtin.IntWireFormat
import hu.simplexion.adaptive.wireformat.signature.Type
import hu.simplexion.adaptive.wireformat.signature.parseSignature

fun AdatPropertyMetaData.toPropertyWireFormat(): AdatPropertyWireFormat<*> =
    AdatPropertyWireFormat(this, parseSignature(signature).toWireFormat())

fun Type.toWireFormat(): WireFormat<*> =
    if (generics.isNotEmpty()) {
        val args = generics.map { WireFormatTypeArgument(it.toWireFormat(), it.nullable)  }
        val genericFormat = checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
        genericFormat.wireFormatCopy(args)
    } else {
        when (name) {
            "Z" -> BooleanWireFormat
            "I" -> IntWireFormat
            else -> checkNotNull(WireFormatRegistry[name]) { "WireFormat for type $name not found" }
        }
    }