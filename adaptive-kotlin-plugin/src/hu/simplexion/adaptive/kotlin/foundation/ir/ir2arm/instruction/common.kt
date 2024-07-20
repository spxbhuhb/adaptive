/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm.instruction

import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.expressions.impl.IrSpreadElementImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

fun IrType.isInstruction(pluginContext: FoundationPluginContext) =
    isSubtypeOfClass(pluginContext.adaptiveInstructionClass) ||
        (isArray() && getArrayElementType(pluginContext.irBuiltIns).isSubtypeOfClass(pluginContext.adaptiveInstructionClass))

fun AdaptiveAnnotationBasedExtension.addInstructions(
    expression: IrCall,
    valueParameters: List<IrValueParameter>,
    instructions: List<IrExpression>
) {

    if (instructions.isEmpty()) return

    val instructionParameter = valueParameters.firstOrNull { it.isInstructions }
    check(instructionParameter != null) { "inner or outer instructions specified but there is no instructions parameter for the fragment" }

    val instructionsArgument = expression.getValueArgument(instructionParameter.index)
        ?: addInstructionsArgument(expression, instructionParameter.index)

    instructionsArgument as IrVarargImpl

    instructions.forEach { instructionsArgument.elements += spreadIfArray(it) }
}

private fun AdaptiveAnnotationBasedExtension.addInstructionsArgument(expression: IrCall, index: Int): IrExpression =
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
