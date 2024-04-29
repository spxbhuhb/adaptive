/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir.impl

import hu.simplexion.z2.kotlin.services.Strings
import hu.simplexion.z2.kotlin.services.ir.ServicesPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType

class NewInstance(
    pluginContext: ServicesPluginContext,
    implClassTransform: ImplClassTransform,
) : AbstractFun(
    pluginContext,
    implClassTransform,
    Strings.NEW_INSTANCE,
    pluginContext.serviceImplNewInstance
) {

    override fun IrSimpleFunction.addParameters() {
        addValueParameter("serviceContext", pluginContext.serviceContextType.makeNullable())
    }

    override fun IrSimpleFunction.buildBody() {
        body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    transformedClass.defaultType,
                    implClassTransform.constructor.symbol,
                    0, 0, 1
                ).also {
                    it.putValueArgument(0, irGet(valueParameters.first()))
                }
            )
        }
    }

}