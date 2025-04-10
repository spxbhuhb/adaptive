/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmDetachExpression
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmValueArgument
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

open class ArmValueArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val valueArgument: ArmValueArgument,
    val closure: ArmClosure,
    val fragment: IrValueParameter,
    val closureDirtyMask: IrVariable
) : ClassBoundIrBuilder(parent) {

    open fun genPatchDescendantExpression(patchFun: IrSimpleFunction): IrExpression? =
        if (valueArgument.isInstructions && valueArgument.instructions.isEmpty()) {
            null
        } else {
            irIf(
                patchCondition(),
                patchVariableValue(patchFun)
            )
        }

    fun patchCondition(): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(fragment),
            args = arrayOf(
                irGet(closureDirtyMask),
                patchDirtyMask()
            )
        )

    /**
     * Calculate the dirty mask for this argument. Special cases may override to ensure that
     * there is no unnecessary patching:
     *
     * - hard-coded sequence (this might change in the future)
     * - lambda fragment factories
     */
    open fun patchDirtyMask() =
        valueArgument.dependencies.toDirtyMask()

    open fun patchVariableValue(patchFun: IrSimpleFunction): IrExpression {
        valueArgument.detachExpressions.forEach { transformDetachExpression(patchFun, it) }

        val valueExpression: IrExpression

        if (valueArgument.isInstructions) {
            valueExpression = buildInstructionGroup(patchFun, valueArgument)
        } else {
            valueExpression = valueArgument.irExpression.transformCreateStateAccess(closure, patchFun) { irGet(fragment) }
        }

        return irSetDescendantStateVariable(
            patchFun,
            valueArgument.argumentIndex,
            valueExpression
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

    fun buildInstructionGroup(patchFun: IrSimpleFunction, valueArgument: ArmValueArgument): IrExpression =
        IrConstructorCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            pluginContext.adaptiveInstructionGroupType,
            pluginContext.adaptiveInstructionGroupConstructor,
            0, 0
        ).apply {

            val argument = IrVarargImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.arrayClass.typeWith(pluginContext.adaptiveInstructionType),
                pluginContext.adaptiveInstructionType
            ).also {
                for (instruction in valueArgument.instructions) {
                    it.elements += instruction.transformCreateStateAccess(closure, patchFun) { irGet(fragment) }
                }
            }

            putValueArgument(0, argument)
        }


}