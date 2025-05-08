/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.service.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.common.AbstractPluginContext
import `fun`.adaptive.kotlin.common.functionByName
import `fun`.adaptive.kotlin.common.property
import `fun`.adaptive.kotlin.common.propertyGetter
import `fun`.adaptive.kotlin.service.CallableIds
import `fun`.adaptive.kotlin.service.ClassIds
import `fun`.adaptive.kotlin.service.Strings
import `fun`.adaptive.kotlin.wireformat.WireFormatCache
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.constructors

class ServicesPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val serviceConsumerClass = ClassIds.SERVICE_CONSUMER.classSymbol()

    val serviceImplClass = ClassIds.SERVICE_IMPL.classSymbol()
    val serviceImplType = serviceImplClass.defaultType
    val serviceImplConstructor = serviceImplClass.constructors.first()
    val serviceImplNewInstance = serviceImplClass.functionByName { Strings.NEW_INSTANCE }
    val serviceImplFragment = serviceImplClass.property { Strings.FRAGMENT }

    val getService = CallableIds.GET_SERVICE.firstFunctionSymbol()

    val serviceContextClass = ClassIds.SERVICE_CONTEXT.classSymbol()
    val serviceContextType = serviceContextClass.defaultType

    val wireFormatCache = WireFormatCache(this)

}