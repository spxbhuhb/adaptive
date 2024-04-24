/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.services.*
import hu.simplexion.z2.kotlin.services.ir.util.ServiceFunctionCache
import hu.simplexion.z2.kotlin.common.AbstractPluginContext
import hu.simplexion.z2.kotlin.wireformat.WireFormatCache
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType

class ServicesPluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    val serviceClass = ClassIds.SERVICE.classSymbol()
    val serviceType = serviceClass.defaultType
    val getWireFormatEncoder = serviceClass.propertyGetter { Strings.WIREFORMAT_ENCODER }
    val getWireFormatStandalone = serviceClass.propertyGetter { Strings.WIREFORMAT_STANDALONE }
    val callService = serviceClass.functionByName { Strings.CALL_SERVICE }

    val serviceImplClass = ClassIds.SERVICE_IMPL.classSymbol()
    val serviceImplType = serviceImplClass.defaultType
    val serviceImplNewInstance = serviceImplClass.functionByName { Strings.NEW_INSTANCE }

    val getService = CallableIds.GET_SERVICE.firstFunctionSymbol()

    val serviceContextClass = ClassIds.SERVICE_CONTEXT.classSymbol()
    val serviceContextType = serviceContextClass.defaultType

    val serviceFunctionCache = ServiceFunctionCache()

    val wireFormatCache = WireFormatCache(this)
}