/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.services.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import hu.simplexion.adaptive.kotlin.common.functionByName
import hu.simplexion.adaptive.kotlin.services.CallableIds
import hu.simplexion.adaptive.kotlin.services.ClassIds
import hu.simplexion.adaptive.kotlin.services.Strings
import hu.simplexion.adaptive.kotlin.wireformat.WireFormatCache
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType

class ServicesPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val serviceClass = ClassIds.SERVICE.classSymbol()

    val serviceImplClass = ClassIds.SERVICE_IMPL.classSymbol()
    val serviceImplNewInstance = serviceImplClass.functionByName { Strings.NEW_INSTANCE }

    val getService = CallableIds.GET_SERVICE.firstFunctionSymbol()

    val serviceContextClass = ClassIds.SERVICE_CONTEXT.classSymbol()
    val serviceContextType = serviceContextClass.defaultType

    val wireFormatCache = WireFormatCache(this)
}