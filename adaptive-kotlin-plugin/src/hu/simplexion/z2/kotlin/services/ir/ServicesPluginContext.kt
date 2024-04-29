/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.common.AbstractPluginContext
import hu.simplexion.z2.kotlin.common.functionByName
import hu.simplexion.z2.kotlin.services.CallableIds
import hu.simplexion.z2.kotlin.services.ClassIds
import hu.simplexion.z2.kotlin.services.Strings
import hu.simplexion.z2.kotlin.wireformat.WireFormatCache
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType

class ServicesPluginContext(
    irContext: IrPluginContext,
    options: Z2Options
) : AbstractPluginContext(irContext, options) {

    val serviceClass = ClassIds.SERVICE.classSymbol()

    val serviceImplClass = ClassIds.SERVICE_IMPL.classSymbol()
    val serviceImplNewInstance = serviceImplClass.functionByName { Strings.NEW_INSTANCE }

    val getService = CallableIds.GET_SERVICE.firstFunctionSymbol()

    val serviceContextClass = ClassIds.SERVICE_CONTEXT.classSymbol()
    val serviceContextType = serviceContextClass.defaultType

    val wireFormatCache = WireFormatCache(this)
}