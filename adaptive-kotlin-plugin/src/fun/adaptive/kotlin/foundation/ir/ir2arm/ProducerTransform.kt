/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmDependencies
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmStateVariable
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.util.hasAnnotation

/**
 * Replaces the producer call (of there are any) with a call to `getProducedValue`.
 * Dispatch receiver and parameters of the `getProducedValue` call are not set,
 * `ProducerPostProcessTransform` does that.
 *
 * After this transform it is safe to calculate dependencies on the initialization
 * as the producer call is not there anymore.
 */
class ProducerTransform(
    override val pluginContext: FoundationPluginContext,
    val variable: IrVariable,
    val closure: ArmClosure,
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    var producerCall: IrCall? = null
    var producerDependencies: ArmDependencies? = null

    fun IrElement.dependencies(): List<ArmStateVariable> {
        val visitor = DependencyVisitor(closure, skipLambdas = false)
        accept(visitor, null)
        return visitor.dependencies
    }

    override fun visitCall(expression: IrCall): IrExpression {

        if (expression.symbol.owner.hasAnnotation(pluginContext.producerAnnotation)) {

            check(producerCall == null) { "only one producer call is allowed per state variable" }

            producerCall = expression

            producerDependencies =
                if (variable.hasAnnotation(pluginContext.independentAnnotation)) {
                    emptyList()
                } else {
                    expression.dependencies()
                }

            return irImplicitAs(
                expression.type,
                IrCallImpl(
                    expression.startOffset, expression.endOffset,
                    irBuiltIns.anyNType,
                    pluginContext.getProducedValue,
                    typeArgumentsCount = 0,
                    expression.origin
                )
            )
        }

        return super.visitCall(expression)
    }


}