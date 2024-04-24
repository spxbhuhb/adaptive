package hu.simplexion.z2.kotlin.wireformat

import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol

data class WireFormatType(
    val encode: IrSimpleFunctionSymbol,
    val decode: IrSimpleFunctionSymbol,

    val encodeOrNull: IrSimpleFunctionSymbol,
    val decodeOrNull: IrSimpleFunctionSymbol,

    val encodeList: IrSimpleFunctionSymbol,
    val decodeList: IrSimpleFunctionSymbol,

    val encodeListOrNull: IrSimpleFunctionSymbol,
    val decodeListOrNull: IrSimpleFunctionSymbol,

    val standaloneEncode: IrSimpleFunctionSymbol,
    val standaloneDecode: IrSimpleFunctionSymbol,
    val standaloneDecodeOrNull: IrSimpleFunctionSymbol,

    val standaloneEncodeList: IrSimpleFunctionSymbol,
    val standaloneDecodeList: IrSimpleFunctionSymbol,
    val standaloneDecodeListOrNull: IrSimpleFunctionSymbol,

    /**
     * The signature of the given type.
     */
    val signature: String,

    /**
     * When this type uses the `instance` methods for encoding and decoding,
     * [wireFormat] contains the class symbol of the class that implements
     * `WireFormat`.
     */
    val wireFormat: IrClassSymbol? = null
)

