/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.arm2ir

import hu.simplexion.adaptive.kotlin.base.Indices
import hu.simplexion.adaptive.kotlin.base.Names
import hu.simplexion.adaptive.kotlin.base.Strings
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmClosure
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmStateVariable
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmSupportStateVariable
import hu.simplexion.adaptive.kotlin.common.property
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name

class SupportFunctionTransform(
    private val irBuilder: ClassBoundIrBuilder,
    private val closure: ArmClosure,
    private val declaringFragment: () -> IrExpression,
    private val receivingFragment: IrVariable,
    private val arguments: IrVariable
) : IrElementTransformerVoidWithContext() {

    val pluginContext = irBuilder.pluginContext

    val irContext = irBuilder.irContext

    val irBuiltIns = irContext.irBuiltIns

    override fun visitGetValue(expression: IrGetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return super.visitGetValue(expression)

        if (stateVariable is ArmSupportStateVariable) {
            return getInvokeArgument(stateVariable)
        } else {
            return getStateVariable(stateVariable)
        }
    }

    fun getInvokeArgument(stateVariable: ArmSupportStateVariable): IrExpression {
        val type = irBuilder.stateVariableType(stateVariable)

        return irBuilder.irImplicitAs(
            type,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                pluginContext.arrayGet,
                typeArgumentsCount = 0,
                valueArgumentsCount = 1
            ).also {

                it.dispatchReceiver = irBuilder.irGet(arguments)

                it.putValueArgument(
                    0,
                    irBuilder.irConst(stateVariable.indexInState)
                )
            }
        )
    }

    fun getStateVariable(stateVariable: ArmStateVariable): IrExpression {
        val type = irBuilder.stateVariableType(stateVariable)

        return irBuilder.irImplicitAs(
            type,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                pluginContext.getCreateClosureVariable,
                0,
                Indices.GET_CLOSURE_VARIABLE_ARGUMENT_COUNT
            ).also {

                it.dispatchReceiver = irBuilder.irGet(receivingFragment)

                it.putValueArgument(
                    Indices.GET_CLOSURE_VARIABLE_INDEX,
                    irBuilder.irConst(stateVariable.indexInClosure)
                )
            }
        )
    }

    override fun visitSetValue(expression: IrSetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return super.visitSetValue(expression)

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            0,
            Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also {

            it.dispatchReceiver = declaringFragment()

            it.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irBuilder.irConst(stateVariable.indexInState)
            )

            it.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                expression.value.transform(this, null)
            )
        }
    }

    override fun visitReturn(expression: IrReturn): IrExpression {
        // FIXME support function return
        return expression.value.transform(this, null)
    }

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol !in pluginContext.helperFunctions) {
            return super.visitCall(expression)
        }

        return when (expression.symbol.owner.name.identifier) {
            Strings.HELPER_ADAPTER -> getPropertyValue(Names.HELPER_ADAPTER)
            Strings.HELPER_FRAGMENT -> declaringFragment()
            Strings.HELPER_THIS_STATE -> declaringFragment()
            else -> throw IllegalStateException("unknown helper function: ${expression.symbol}")
        }
    }

    fun getPropertyValue(name: Name) =
        irBuilder.irGetValue(irBuilder.irClass.property(name), declaringFragment())

}
