/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.common.property
import `fun`.adaptive.kotlin.common.propertyGetter
import `fun`.adaptive.kotlin.foundation.ClassIds
import `fun`.adaptive.kotlin.foundation.Indices
import `fun`.adaptive.kotlin.foundation.Names
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmStateVariable
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmWhenStateVariable
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationBase
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImplWithShape
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.parents
import org.jetbrains.kotlin.name.Name

@OptIn(UnsafeDuringIrConstructionAPI::class)
class StateAccessTransform(
    private val irBuilder: ClassBoundIrBuilder,
    private val closure: ArmClosure,
    private val getVariableFunction: IrSimpleFunctionSymbol,
    private val newParent: IrFunction?, // the patch function
    private val irGetFragment: () -> IrExpression
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override val pluginContext = irBuilder.pluginContext

    var stateChange = false
    var inLambda = false

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
            variableCastType(stateVariable),
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.anyNType,
                getVariableFunction,
                0
            ).also {

                it.dispatchReceiver = irGetFragment()

                it.putValueArgument(
                    Indices.GET_CLOSURE_VARIABLE_INDEX,
                    irBuilder.irConst(stateVariable.indexInClosure)
                )
            }
        )

    private fun variableCastType(stateVariable: ArmStateVariable): IrType =
        if (stateVariable.isInstructions) {
            pluginContext.adaptiveInstructionGroupType
        } else {
            stateVariable.type
        }

    override fun visitSetValue(expression: IrSetValue): IrExpression {

        val name = expression.symbol.owner.name.identifier

        val stateVariable = closure.firstOrNull { it.name == name } ?: return super.visitSetValue(expression)

        stateChange = true

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0
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
            Strings.HELPER_ADAPTER -> getPropertyValue(Names.ADAPTER)
            Strings.HELPER_FRAGMENT -> irGet(newParent?.dispatchReceiverParameter!!)
            Strings.HELPER_INSTRUCTIONS -> getInstructions()
            else -> throw IllegalStateException("unknown helper function: ${expression.symbol}")
        }


    fun getPropertyValue(name: Name) =
        irBuilder.irGetValue(irBuilder.irClass.property(name), irGet(newParent?.dispatchReceiverParameter!!))

    fun getInstructions() : IrExpression {
        return irCall(
            irBuilder.irClass.propertyGetter { Strings.INSTRUCTIONS },
            irGet(newParent?.dispatchReceiverParameter!!)
        )
    }

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
     *
     */
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (declaration.origin != IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA) {
            return super.visitFunctionNew(declaration)
        }

        if (inLambda || declaration.parents.contains(newParent as IrDeclarationParent)) {
            return super.visitFunctionNew(declaration)
        }

        inLambda = true
        stateChange = false

        // debugParents(declaration.returnType.render(), declaration)
        // This check is necessary for nested lambdas, otherwise we modify the parent when it should be kept the same.
        // TODO check if nested lambda check can be replaced by level counting or something like that

        declaration.parent = checkNotNull(newParent) { "should not be null here" }

        val transformed = super.visitFunctionNew(declaration)

        if (stateChange) {
            check(declaration.body is IrBlockBody) { "only block body is supported" }

            val body = declaration.body as IrBlockBody

            // FIXME adding patch at the end of the lambda body is wrong on so many levels

            body.statements += IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.unitType,
                pluginContext.patchIfDirty,
                typeArgumentsCount = 0
            ).also {
                it.dispatchReceiver = irGet(newParent.dispatchReceiverParameter !!)
            }
        }

        inLambda = false

        return transformed
    }

    override fun visitFunctionReference(expression: IrFunctionReference): IrExpression {
        val target = expression.reflectionTarget ?: return super.visitFunctionReference(expression)
        if (! target.owner.hasAnnotation(ClassIds.ADAPTIVE)) return super.visitFunctionReference(expression)

        return IrFunctionReferenceImplWithShape(
            expression.startOffset,
            expression.endOffset,
            pluginContext.kFunctionAdaptiveReferenceType,
            expression.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 2,
            contextParameterCount = 0,
            hasDispatchReceiver = false,
            hasExtensionReceiver = false
        )
    }

//
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
