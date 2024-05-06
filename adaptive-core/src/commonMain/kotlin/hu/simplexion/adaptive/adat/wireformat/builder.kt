/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.wireformat

import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.builtin.BooleanWireFormat
import hu.simplexion.adaptive.wireformat.builtin.IntWireFormat
import hu.simplexion.adaptive.wireformat.signature.Type
import hu.simplexion.adaptive.wireformat.signature.parseSignature

fun AdatPropertyMetaData.toPropertyWireFormat(dependencies: List<AdatCompanion<*>>): AdatPropertyWireFormat<*> =
    AdatPropertyWireFormat(this, parseSignature(signature).toWireFormat(dependencies))

fun Type.toWireFormat(dependencies: List<AdatCompanion<*>>): WireFormat<*> =
    if (generics.isEmpty()) {
        when (name) {
            "Z", "Z?" -> BooleanWireFormat
            "I", "I?" -> IntWireFormat
            else -> dependencies.first { it.adatMetadata.name == name }
        }
    } else {

    }