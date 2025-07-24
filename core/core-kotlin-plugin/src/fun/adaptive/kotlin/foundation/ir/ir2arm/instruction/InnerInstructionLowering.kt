/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm.instruction

import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClass
import `fun`.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import `fun`.adaptive.kotlin.foundation.ir.util.HigherOrderProcessing
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.isFunction
import org.jetbrains.kotlin.ir.visitors.IrVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class InnerInstructionLowering(
    override val pluginContext: FoundationPluginContext,
    val armClass: ArmClass
) : IrVisitorVoid(), AdaptiveAnnotationBasedExtension {

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitCall(expression: IrCall) {
        super.visitCall(expression)

        if (! expression.isDirectAdaptiveCall) return

        val valueParameters = expression.symbol.owner.parameters

        @HigherOrderProcessing
        val adaptiveParameter = valueParameters.lastOrNull { it.kind == IrParameterKind.Regular && it.isAdaptive && it.type.isFunction() } ?: return

        val store = armClass.instructions.getOrPut(expression) { mutableListOf() }
        store += extractInnerInstructions(expression.arguments[adaptiveParameter])
    }

    private fun extractInnerInstructions(irExpression: IrExpression?): List<IrExpression> {
        // there cannot be inner instructions for function references
        if (irExpression as? IrFunctionExpression == null) return emptyList()

        val body = irExpression.function.body as? IrBlockBody ?: return emptyList()

        val result = mutableListOf<IrExpression>()

        for (statement in body.statements) {

            if (statement !is IrExpression) break
            if (statement !is IrTypeOperatorCall) break

            if (statement.argument.isRenderingCall()) break

            if (! statement.argument.type.isInstruction(pluginContext)) break

            result += statement.argument
        }

        repeat(result.size) { body.statements.removeAt(0) }

        return result
    }

    private fun IrExpression.isRenderingCall(): Boolean {
        var current = this

        while (current is IrCall && current.dispatchReceiver != null) {
            current = current.dispatchReceiver !!
        }

        return current.type.isSubtypeOfClass(pluginContext.adaptiveFragmentClass)
    }

}
