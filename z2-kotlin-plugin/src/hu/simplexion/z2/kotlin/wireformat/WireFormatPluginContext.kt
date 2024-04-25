/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.wireformat

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.common.AbstractPluginContext
import hu.simplexion.z2.kotlin.common.functionByName
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

class WireFormatPluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    val wireFormatClass = ClassIds.WIREFORMAT.classSymbol()

    val standalone = ClassIds.STANDALONE.classSymbol()

    val wireFormatEncoder = ClassIds.WIREFORMAT_ENCODER.classSymbol()
    val wireFormatDecoder = ClassIds.WIREFORMAT_ENCODER.classSymbol()

    val wireFormatTypeTemplate =
        WireFormatType(
            encode = wireFormatEncoder.functionByName { Strings.INSTANCE },
            decode = wireFormatDecoder.functionByName { Strings.INSTANCE },

            encodeOrNull = wireFormatEncoder.functionByName { Strings.INSTANCE_OR_NULL },
            decodeOrNull = wireFormatDecoder.functionByName { Strings.INSTANCE_OR_NULL },

            encodeList = wireFormatEncoder.functionByName { Strings.INSTANCE_LIST },
            decodeList = wireFormatDecoder.functionByName { Strings.INSTANCE_LIST },

            encodeListOrNull = wireFormatEncoder.functionByName { Strings.INSTANCE_LIST_OR_NULL },
            decodeListOrNull = wireFormatDecoder.functionByName { Strings.INSTANCE_LIST_OR_NULL },

            standaloneEncode = standalone.functionByName { Strings.ENCODE_INSTANCE },
            standaloneDecode = standalone.functionByName { Strings.DECODE_INSTANCE },
            standaloneDecodeOrNull = standalone.functionByName { Strings.DECODE_INSTANCE_OR_NULL },

            standaloneEncodeList = standalone.functionByName { Strings.ENCODE_INSTANCE_LIST },
            standaloneDecodeList = standalone.functionByName { Strings.DECODE_INSTANCE_LIST },
            standaloneDecodeListOrNull = standalone.functionByName { Strings.DECODE_INSTANCE_LIST_OR_NULL },

            signature = "L" // this will be extended when the template is actualized
        )


}