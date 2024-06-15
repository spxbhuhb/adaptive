/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm

import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import hu.simplexion.adaptive.kotlin.foundation.ir.util.HigherOrderProcessing
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrSpreadElementImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.isFunction
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

class InnerInstructionLowering(
    override val pluginContext: FoundationPluginContext
) : IrElementVisitorVoid, AdaptiveAnnotationBasedExtension {

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitCall(expression: IrCall) {
        super.visitCall(expression)

        if (! expression.isDirectAdaptiveCall) return

        val valueParameters = expression.symbol.owner.valueParameters

        @HigherOrderProcessing
        val adaptiveParameter = valueParameters.lastOrNull { it.isAdaptive && it.type.isFunction() } ?: return
        val instructionParameter = valueParameters.firstOrNull { it.isInstructions }

        val innerInstructions = extractInnerInstructions(expression.getValueArgument(adaptiveParameter.index))

        if (innerInstructions.isEmpty()) return
        check(instructionParameter != null) { "inner instructions specified but there is no instructions parameter for the higher order fragment" }

        val instructionsArgument = expression.getValueArgument(instructionParameter.index)
            ?: addInstructionsArgument(expression, instructionParameter.index)

        instructionsArgument as IrVarargImpl

        innerInstructions.forEach { instructionsArgument.elements += spreadIfArray(it) }
    }

    private fun extractInnerInstructions(irExpression: IrExpression?): List<IrExpression> {
        if (irExpression == null) return emptyList()
        irExpression as IrFunctionExpression

        val body = irExpression.function.body as? IrBlockBody ?: return emptyList()

        val result = mutableListOf<IrExpression>()

        for (statement in body.statements) {
            if (statement !is IrExpression) break
            if (statement !is IrTypeOperatorCall) break
            if (! statement.argument.type.isInstruction) break
            result += statement.argument
        }

        repeat(result.size) { body.statements.removeAt(0) }

        return result
    }

    private val IrType.isInstruction
        get() = isSubtypeOfClass(pluginContext.adaptiveInstructionClass) ||
            (isArray() && getArrayElementType(pluginContext.irBuiltIns).isSubtypeOfClass(pluginContext.adaptiveInstructionClass))

    private fun addInstructionsArgument(expression: IrCall, index: Int): IrExpression =
        IrVarargImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            pluginContext.irBuiltIns.arrayClass.typeWith(pluginContext.adaptiveInstructionType),
            pluginContext.adaptiveInstructionType
        ).also {
            expression.putValueArgument(index, it)
        }

    private fun spreadIfArray(instruction: IrExpression): IrVarargElement =
        if (instruction.type.isArray()) {
            IrSpreadElementImpl(
                instruction.startOffset, instruction.endOffset,
                instruction
            )
        } else {
            instruction
        }

}
