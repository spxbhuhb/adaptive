package hu.simplexion.z2.kotlin.services.ir.util

import hu.simplexion.z2.kotlin.common.AbstractIrBuilder
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
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