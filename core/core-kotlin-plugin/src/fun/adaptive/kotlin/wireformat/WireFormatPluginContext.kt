/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.wireformat

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.common.AbstractPluginContext
import `fun`.adaptive.kotlin.common.functionByName
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

class WireFormatPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val wireFormatClass = ClassIds.WIREFORMAT.classSymbol()

    val wireFormatEncoder = ClassIds.WIREFORMAT_ENCODER.classSymbol()
    val wireFormatDecoder = ClassIds.WIREFORMAT_DECODER.classSymbol()

    val asInstance = wireFormatDecoder.functionByName { "asInstance" }
    val asInstanceOrNull = wireFormatDecoder.functionByName { "asInstanceOrNull" }
    val rawInstance = wireFormatEncoder.functionByName { "rawInstance" }
    val rawInstanceOrNull = wireFormatEncoder.functionByName { "rawInstanceOrNull" }
    val pack = wireFormatEncoder.functionByName { "pack" }

    val encodeInstance = wireFormatEncoder.functionByName { Strings.INSTANCE }
    val decodeInstance = wireFormatDecoder.functionByName { Strings.INSTANCE }
    val encodeInstanceOrNull = wireFormatEncoder.functionByName { Strings.INSTANCE_OR_NULL }
    val decodeInstanceOrNull = wireFormatDecoder.functionByName { Strings.INSTANCE_OR_NULL }

    val pseudoInstanceStart = wireFormatEncoder.functionByName { Strings.PSEUDO_INSTANCE_START }
    val pseudoInstanceEnd = wireFormatEncoder.functionByName { Strings.PSEUDO_INSTANCE_END }

    val wireFormatTypeArgument = ClassIds.WIREFORMAT_TYPE_ARGUMENT.classSymbol()
}