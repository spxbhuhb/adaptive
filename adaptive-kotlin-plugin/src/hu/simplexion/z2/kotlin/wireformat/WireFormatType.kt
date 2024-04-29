/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.kotlin.wireformat

import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol

data class WireFormatType(
    val encode: IrSimpleFunctionSymbol, // int
    val decode: IrSimpleFunctionSymbol, // int

    val encodeOrNull: IrSimpleFunctionSymbol, // intOrNull
    val decodeOrNull: IrSimpleFunctionSymbol, // intOrNull

    val primitive: Boolean,

    /**
     * The signature of the given type.
     */
    val signature: String,

    /**
     * When this type uses the `instance` methods for encoding and decoding,
     * [wireFormat] contains the class symbol of the class that implements
     * `WireFormat`.
     */
    val wireFormat: IrClassSymbol
)

