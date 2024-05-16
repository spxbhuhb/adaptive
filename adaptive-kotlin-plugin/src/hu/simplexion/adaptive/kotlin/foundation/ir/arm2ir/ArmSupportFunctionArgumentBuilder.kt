/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmClosure
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmSupportFunctionArgument
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors

class ArmSupportFunctionArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmSupportFunctionArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun patchBody(patchFun : IrSimpleFunction): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            argument.argumentIndex,
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classBoundSupportFunctionType,
                pluginContext.boundSupportFunctionClass.constructors.first(),
                1, 0,
                Indices.ADAPTIVE_SUPPORT_FUNCTION_ARGUMENT_COUNT,
            ).apply {
                putTypeArgument(0, classBoundBridgeType.defaultType)
                putValueArgument(Indices.ADAPTIVE_SUPPORT_FUNCTION_DECLARING_FRAGMENT, irGet(patchFun.dispatchReceiverParameter!!))
                putValueArgument(Indices.ADAPTIVE_SUPPORT_FUNCTION_RECEIVING_FRAGMENT, irGet(patchFun.valueParameters[Indices.PATCH_DESCENDANT_FRAGMENT]))
                putValueArgument(Indices.ADAPTIVE_SUPPORT_FUNCTION_INDEX, irConst(argument.supportFunctionIndex))
            }
        )

}