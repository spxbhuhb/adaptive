/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmClosure
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmDetachExpression
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmValueArgument
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

open class ArmValueArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val valueArgument: ArmValueArgument,
    val closure: ArmClosure,
    val fragment: IrValueParameter,
    val closureDirtyMask: IrVariable
) : ClassBoundIrBuilder(parent) {

    open fun genPatchDescendantExpression(patchFun: IrSimpleFunction): IrExpression? =
        irIf(
            patchCondition(),
            patchBody(patchFun)
        )

    fun patchCondition(): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(fragment),
            args = arrayOf(
                irGet(closureDirtyMask),
                valueArgument.dependencies.toDirtyMask()
            )
        )

    // TODO rename patchBody, the `body` is confusing
    open fun patchBody(patchFun: IrSimpleFunction): IrExpression {
        valueArgument.detachExpressions.forEach { transformDetachExpression(patchFun, it) }

        return irSetDescendantStateVariable(
            patchFun,
            valueArgument.argumentIndex,
            valueArgument.irExpression.transformCreateStateAccess(closure, patchFun) { irGet(fragment) }
        )
    }

    fun transformDetachExpression(patchFun: IrSimpleFunction, detachExpression: ArmDetachExpression) {
        val lambda = detachExpression.lambda.function

        lambda.body = DeclarationIrBuilder(irContext, lambda.symbol).irBlockBody {
            + irCall(
                pluginContext.detachFun,
                irGet(lambda.valueParameters.first()), // the handler
                irGet(patchFun.dispatchReceiverParameter !!), // first parameter of the handler
                irConst(detachExpression.armCall.index)
            )
        }
    }
}