/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.ir2arm.instruction

import `fun`.adaptive.kotlin.common.firstRegularArgument
import `fun`.adaptive.kotlin.foundation.Names
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClass
import `fun`.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.util.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class OuterInstructionLowering(
    override val pluginContext: FoundationPluginContext,
    val armClass: ArmClass
) : IrElementTransformerVoid(), AdaptiveAnnotationBasedExtension {

    override fun visitTypeOperator(expression: IrTypeOperatorCall): IrExpression {

        if (expression.operator != IrTypeOperator.IMPLICIT_COERCION_TO_UNIT) {
            return super.visitTypeOperator(expression)
        }

        val argument = expression.argument

        if (argument !is IrCall) {
            return super.visitTypeOperator(expression)
        }

        return visitCall(argument)
    }

    override fun visitCall(expression: IrCall): IrExpression {

        // All outer instructions use rangeTo and if a fragment call has outer instructions,
        // the first `visitCall` will be called for the last `rangeTo` in the call chain.

        val owner = expression.symbol.owner
        if (! owner.isOperator || owner.name != Names.RANGE_TO) {
            return super.visitCall(expression)
        }

        // If the result of `rangeTo` is not a fragment we surely do not have to lower.

        if (! expression.type.isSubtypeOfClass(pluginContext.adaptiveFragmentClass)) {
            return super.visitCall(expression)
        }

        // There are different cases to handle:
        //
        // Normal outer instructions:
        //   case 1:    a() .. name("1")
        //   case 2:    a() .. name("1") .. name("2")
        //
        // Outer instructions in adaptive lambdas:
        //   case 3:    a { b .. name("1") }
        //
        // Instructions in arguments -- we should NOT lower these:
        //   case 4:    a(name("1") .. name("2"))                 -- rangeTo type is not fragment, handled above
        //   case 5:    a(fragment().instructions .. name("3"))   -- rangeTo type is not fragment, handled above
        //   case 6:    a(fragment() .. name("3"))                -- this is actually an error, TODO report bug when outer instruction is used with fragment()
        //
        // The problematic is the instructions in the arguments, especially the last case
        // where it is particularly hard to decide what to do. In the case of arguments we
        // should not lower the instructions.
        //
        // TODO Think about ultimate receivers in OuterInstructionLowering. Is it true that we have to lower when it is a fragment call?
        // We have to lower when the receiver of the first (by source code) `rangeTo` is a fragment call.

        val (renderCall, instructions) = flattenInstructionCalls(expression)

        // note that `renderCall` is used here, not `expression`
        val store = armClass.instructions.getOrPut(renderCall) { mutableListOf() }
        store += instructions

        return super.visitCall(renderCall)
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
     * @return Pair(render call, list of instruction expressions), null when this is not an outer instruction chain
     */
    fun flattenInstructionCalls(expression: IrCall): Pair<IrCall, List<IrExpression>> {

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

            val receiver = current.dispatchReceiver
            checkNotNull(receiver) { "invalid outer instruction chain (missing receiver): ${expression.dumpKotlinLike()}" }

            // TODO cache rangeTo operator functions?
            val owner = current.symbol.owner
            check(owner.isOperator) { "invalid outer instruction chain (not an operator): ${expression.dumpKotlinLike()}" }
            check(owner.name == Names.RANGE_TO) { "invalid outer instruction chain (not rangeTo): ${expression.dumpKotlinLike()}" }

            val value = current.firstRegularArgument !!
            val type = value.type

            check(type.isInstruction(pluginContext)) { "invalid outer instruction chain (range end is not an instruction): ${expression.dumpKotlinLike()}" }

            instructions += value

            check(receiver is IrCall) { "invalid outer instruction chain (receiver is not a call): ${expression.dumpKotlinLike()}" }

            if (chainEnd(receiver)) {

                check(receiver.isDirectAdaptiveCall || receiver.isArgumentAdaptiveCall) { "invalid outer instruction chain (not a rendering call): ${expression.dumpKotlinLike()}" }

                instructions.reverse()

                return receiver to instructions
            }

            current = receiver
        }

        throw IllegalStateException("invalid transform chain (missing fragment call): ${expression.dumpKotlinLike()}")
    }

    fun chainEnd(receiver: IrCall) : Boolean {
        val receiverOwner = receiver.symbol.owner

        if (receiverOwner.name == Names.KOTLIN_INVOKE) return true
        if (receiverOwner.isOperator) return false
        if (receiver.type.isSubtypeOfClass(pluginContext.adaptiveFragmentClass)) return true

        return false
    }
}
