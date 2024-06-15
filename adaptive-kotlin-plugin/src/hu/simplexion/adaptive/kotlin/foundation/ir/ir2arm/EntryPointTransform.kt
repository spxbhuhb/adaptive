/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm

import hu.simplexion.adaptive.kotlin.foundation.FqNames
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmEntryPoint
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.util.hasAnnotation

/**
 * Creates an `ArmClass` and a `ArmEntryPoint` for each call of the `adaptive` function (defined in the runtime).
 */
class EntryPointTransform(
    private val pluginContext: FoundationPluginContext
) : IrElementTransformerVoidWithContext() {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

    /**
     * Transforms Adaptive entry points (calls to the function `adaptive`) into an
     * Adaptive root class and modifies the last parameter of the function (which
     * has to be a lambda) so it gets an adapter and creates a new instance
     * of the root class.
     */
    override fun visitCall(expression: IrCall): IrExpression {
        if (!expression.symbol.owner.hasAnnotation(FqNames.ADAPTIVE_ENTRY)) {
            return super.visitCall(expression)
        }

        check(expression.valueArgumentsCount != 0) { "${expression.symbol} value arguments count == 0" }

        val block = expression.getValueArgument(expression.valueArgumentsCount - 1)

        check(block is IrFunctionExpression) { "${expression.symbol} last parameter is not a function expression" }

        val function = block.function

        val armClass = IrFunction2ArmClass(pluginContext, block.function, true).transform()

        ArmEntryPoint(armClass, function).also {
            pluginContext.armEntryPoints += it
        }

        return expression
    }

}
