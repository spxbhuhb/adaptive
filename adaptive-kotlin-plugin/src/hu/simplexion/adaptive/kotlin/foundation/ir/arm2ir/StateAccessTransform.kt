/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.property
import hu.simplexion.adaptive.kotlin.foundation.ClassIds
import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.Names
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmClosure
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmInternalStateVariable
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmStateVariable
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmWhenStateVariable
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationBase
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name

@OptIn(UnsafeDuringIrConstructionAPI::class) // uncomment for 2.0.0.
class StateAccessTransform(
    private val irBuilder: ClassBoundIrBuilder,
    private val closure: ArmClosure,
    private val getVariableFunction: IrSimpleFunctionSymbol,
    private val newParent: IrFunction?,
    private val irGetFragment: () -> IrExpression,
    private val stateVariable: ArmInternalStateVariable? = null
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override val pluginContext = irBuilder.pluginContext

    override fun visitGetValue(expression: IrGetValue): IrExpression {
        val name = expression.symbol.owner.name
        if (name.isSpecial) return super.visitGetValue(expression)

        val id = name.identifier

        val stateVariable = closure.firstOrNull { it.name == id } ?: return expression

        if (stateVariable is ArmWhenStateVariable) {
            return irBuilder.irGet(stateVariable.irVariable)
        } else {
            return getStateVariable(stateVariable)
        }
    }

    fun getStateVariable(stateVariable: ArmStateVariable): IrExpression =
        irBuilder.irImplicitAs(
            stateVariable.type,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                getVariableFunction,
                0,
                Indices.GET_CLOSURE_VARIABLE_ARGUMENT_COUNT
            ).also {

                it.dispatchReceiver = irGetFragment()

                it.putValueArgument(
                    Indices.GET_CLOSURE_VARIABLE_INDEX,
                    irBuilder.irConst(stateVariable.indexInClosure)
                )
            }
        )

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

            it.dispatchReceiver = irGet(newParent !!.dispatchReceiverParameter !!)

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

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol in pluginContext.helperFunctions) {
            return transformHelper(expression)
        }

        return super.visitCall(expression)
    }

    fun transformHelper(expression: IrCall) =
        when (expression.symbol.owner.name.identifier) {
            Strings.HELPER_ADAPTER -> getPropertyValue(Names.HELPER_ADAPTER)
            Strings.HELPER_FRAGMENT -> irGetFragment()
            Strings.HELPER_THIS_STATE -> irGetFragment()
            else -> throw IllegalStateException("unknown helper function: ${expression.symbol}")
        }


    fun getPropertyValue(name: Name) =
        irBuilder.irGetValue(irBuilder.irClass.property(name), irGetFragment())

    override fun visitDeclaration(declaration: IrDeclarationBase): IrStatement {
        if (declaration.parent == irBuilder.armClass.originalFunction) {
            declaration.parent = checkNotNull(newParent) { "cannot transform parent: $declaration" }
        }
        return super.visitDeclaration(declaration)
    }

    /**
     * Set parent of lambda functions:
     *
     * - when in state definition
     *   - sets parent to `genPatchInternal`
     * - when in rendering
     *   - sets parent to `genPatchDescendant`
     */
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (declaration.origin == IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA) {
            declaration.parent = checkNotNull(newParent) { "should not be null here" }
        }
        return super.visitFunctionNew(declaration)
    }

//    fun debugParents(label: String, declaration: IrDeclaration) {
//        pluginContext.debug(label) {
//            declaration.parentsWithSelf.toList().reversed().joinToString {
//                it::class.simpleName + ": " +
//                    when (it) {
//                        is IrFileImpl -> it.name
//                        is IrClassImpl -> it.name.toString()
//                        is IrFunctionImpl -> it.name.toString()
//                        is IrFunctionWithLateBindingImpl -> it.name.toString()
//                        else -> it.toString()
//                    }
//            }
//        }
//    }

}
