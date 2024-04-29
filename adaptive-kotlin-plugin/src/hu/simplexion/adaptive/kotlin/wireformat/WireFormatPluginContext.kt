/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.wireformat

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import hu.simplexion.adaptive.kotlin.common.functionByName
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

class WireFormatPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val wireFormatClass = ClassIds.WIREFORMAT.classSymbol()

    val wireFormatEncoder = ClassIds.WIREFORMAT_ENCODER.classSymbol()
    val wireFormatDecoder = ClassIds.WIREFORMAT_DECODER.classSymbol()

    val asInstance = wireFormatDecoder.functionByName { "asInstance" }
    val rawInstance = wireFormatEncoder.functionByName { "rawInstance" }
    val pack = wireFormatEncoder.functionByName { "pack" }

    val encodeInstance = wireFormatEncoder.functionByName { Strings.INSTANCE }
    val decodeInstance = wireFormatDecoder.functionByName { Strings.INSTANCE }
    val encodeInstanceOrNull = wireFormatEncoder.functionByName { Strings.INSTANCE_OR_NULL }
    val decodeInstanceOrNull = wireFormatDecoder.functionByName { Strings.INSTANCE_OR_NULL }

}