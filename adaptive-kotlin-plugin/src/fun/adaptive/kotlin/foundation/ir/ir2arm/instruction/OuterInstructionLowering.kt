/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm.instruction

import `fun`.adaptive.kotlin.foundation.Names
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class OuterInstructionLowering(
    override val pluginContext: FoundationPluginContext
) : IrElementTransformerVoid(), AdaptiveAnnotationBasedExtension {

    override fun visitTypeOperator(expression: IrTypeOperatorCall): IrExpression {
        if (expression.operator != IrTypeOperator.IMPLICIT_COERCION_TO_UNIT) return expression

        // TODO make outer instruction recognition better, this is quite dirty I think
        val argument = expression.argument

        if (argument !is IrCall) return expression

        val extensionReceiver = argument.extensionReceiver ?: return expression
        if (! extensionReceiver.type.isSubtypeOfClass(pluginContext.adaptiveFragmentClass)) return expression

        val (renderCall, instructions) = flattenInstructionCalls(argument)

        val valueParameters = renderCall.symbol.owner.valueParameters

        addInstructions(renderCall, valueParameters, instructions)

        return renderCall
    }

    /**
     * Flatten a call chain for `rangeTo` operators and instructions. For example:
     * `text("a") .. name("12") .. bold .. italic`
     *
     * These should be like this in IR:
     *
     * ```text
     * irCall                           -- rangeTo
     *   receiver:
     *     irCall                       -- rangeTo
     *       receiver:
     *           irCall                 -- rangeTo
     *               receiver:
     *                  irCall          -- original function
     *               instruction        -- name("12")
     *       instruction:               -- bold
     *   instruction:                   -- italic
     * ```
     *
     * @return Pair(render call, list of instruction expressions)
     */
    fun flattenInstructionCalls(expression: IrExpression): Pair<IrCall, List<IrExpression>> {
        check(expression is IrCall)

        //  TYPE_OP type=kotlin.Unit origin=IMPLICIT_COERCION_TO_UNIT typeOperand=kotlin.Unit
        //    CALL 'public final fun rangeTo (instruction: `fun`.adaptive.foundation.instruction.AdaptiveInstruction): `fun`.adaptive.foundation.AdaptiveFragment [operator] declared in `fun`.adaptive.foundation' type=`fun`.adaptive.foundation.AdaptiveFragment origin=RANGE
        //      $receiver: CALL 'public final fun testFragment (vararg instructions: `fun`.adaptive.foundation.instruction.AdaptiveInstruction, block: kotlin.Function0<kotlin.Unit>): `fun`.adaptive.foundation.AdaptiveFragment declared in stuff' type=`fun`.adaptive.foundation.AdaptiveFragment origin=null
        //        block: FUN_EXPR type=kotlin.Function0<kotlin.Unit> origin=LAMBDA
        //          FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:kotlin.Unit
        //            BLOCK_BODY
        //              RETURN type=kotlin.Nothing from='local final fun <anonymous> (): kotlin.Unit declared in stuff.box.<anonymous>'
        //                GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
        //      instruction: CALL 'public final fun name (name: kotlin.String): `fun`.adaptive.foundation.instruction.Name declared in `fun`.adaptive.foundation.instruction' type=`fun`.adaptive.foundation.instruction.Name origin=null
        //        name: CONST String type=kotlin.String value="stuff - 1"

        val instructions = mutableListOf<IrExpression>()

        var current: IrCall? = expression

        while (current != null) {

            if (current.extensionReceiver == null) {

                check(current.isDirectAdaptiveCall || current.isArgumentAdaptiveCall) {
                    "invalid outer instruction chain (not a rendering call): ${expression.dumpKotlinLike()}"
                }

                instructions.reverse()

                return current to instructions
            }

            // TODO cache rangeTo operator functions?
            val owner = current.symbol.owner

            check(owner.isOperator) { "invalid outer instruction chain (not an operator): ${expression.dumpKotlinLike()}" }

            check(owner.name == Names.RANGE_TO) { "invalid outer instruction chain (not rangeTo): ${expression.dumpKotlinLike()}" }

            val value = current.getValueArgument(0) !!
            val type = value.type

            check(type.isInstruction(pluginContext)) { "invalid outer instruction chain (range end is not an instruction): ${expression.dumpKotlinLike()}" }

            instructions += value

            current = current.extensionReceiver as? IrCall
        }

        error { "invalid transform chain (missing fragment call): ${expression.dumpKotlinLike()}" }
    }
}
