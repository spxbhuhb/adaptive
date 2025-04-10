/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.service.ir.util

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.service.ir.ServicesPluginContext
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol

interface ServiceBuilder : AbstractIrBuilder {

    override val pluginContext: ServicesPluginContext

    var serviceNameGetter: IrSimpleFunctionSymbol

    fun getServiceName(function: IrSimpleFunction): IrCallImpl =
        irCall(
            serviceNameGetter,
            irGet(checkNotNull(function.dispatchReceiverParameter))
        ).also {
            it.origin = IrStatementOrigin.GET_PROPERTY
        }


}