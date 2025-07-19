/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.adat.CallableIds
import `fun`.adaptive.kotlin.adat.ClassIds
import `fun`.adaptive.kotlin.adat.ir.immutable.ImmutableCache
import `fun`.adaptive.kotlin.common.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.util.getSimpleFunction

class AdatPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val shortImmutableCache = ImmutableCache(true)
    val qualifiedImmutableCache = ImmutableCache(false)

    val adatClassWireFormat = ClassIds.ADAT_CLASS_WIREFORMAT.classSymbol()

    val wireFormatRegistry = ClassIds.WIREFORMAT_REGISTRY.classSymbol()
    val wireFormatRegistrySet = checkNotNull(wireFormatRegistry.getSimpleFunction("set"))

    val arrayGet = checkNotNull(irContext.irBuiltIns.arrayClass.getSimpleFunction("get"))

    val propertiesFun = CallableIds.properties.functions().first()

}