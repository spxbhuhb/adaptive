/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.ir2arm

import hu.simplexion.adaptive.kotlin.base.FqNames
import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmEntryPoint
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.util.kotlinFqName

/**
 * Creates an `ArmClass` and a `ArmEntryPoint` for each call of the `adaptive` function (defined in the runtime).
 */
class EntryPointTransform(
    private val adaptiveContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = adaptiveContext.irContext.irBuiltIns

    /**
     * Transforms Adaptive entry points (calls to the function `adaptive`) into an
     * Adaptive root class and modifies the last parameter of the function (which
     * has to be a lambda) so it gets an adapter and creates a new instance
     * of the root class.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol.owner.kotlinFqName != FqNames.ADAPTIVE_ENTRY_FUNCTION) {
            return super.visitCall(expression)
        }

        check(expression.valueArgumentsCount != 0) { "${expression.symbol} value arguments count == 0" }

        val block = expression.getValueArgument(expression.valueArgumentsCount - 1)

        check(block is IrFunctionExpression) { "${expression.symbol} last parameter is not a function expression" }

        val function = block.function

        val armClass = IrFunction2ArmClass(adaptiveContext, block.function, true).transform()

        ArmEntryPoint(armClass, function).also {
            adaptiveContext.armEntryPoints += it
        }

        return expression
    }

}
