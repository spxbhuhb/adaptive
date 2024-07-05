/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.adat.ir.metadata

import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import org.jetbrains.kotlin.ir.declarations.IrProperty

class PropertyData(
    val property: IrProperty,
    val metadata: AdatPropertyMetadata
)