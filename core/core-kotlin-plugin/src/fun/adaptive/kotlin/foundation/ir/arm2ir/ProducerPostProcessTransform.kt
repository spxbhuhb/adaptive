/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmDependencies
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmInternalStateVariable
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmStateVariable
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrVisitorVoid

/**
 * Sets dispatch receiver and parameters of the `getProducedValue` call are not set, that
 * is in `StateAccessTransform`.
 *
 * After this transform it is safe to calculate dependencies on the initialization
 * as the producer call is not there anymore.
 */
class ProducerPostProcessTransform(
    override val pluginContext: FoundationPluginContext,
    val patchFun : IrFunction,
    val stateVariable: ArmInternalStateVariable
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitCall(expression: IrCall) : IrExpression {
        if (expression.symbol == pluginContext.getProducedValue) {
            expression.arguments[0] = irGet(patchFun.dispatchReceiverParameter!!)
            expression.arguments[1] = irConst(stateVariable.indexInState)
        }
        return super.visitCall(expression)
     }


}