/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.Names
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.*
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.common.property
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

open class ClassBoundIrBuilder(
    override val pluginContext: FoundationPluginContext,
    val armClass : ArmClass
) : AbstractIrBuilder {

    constructor(parent: ClassBoundIrBuilder) : this(parent.pluginContext, parent.armClass) {
        this.irClass = parent.irClass
    }

    lateinit var irClass: IrClass

    // --------------------------------------------------------------------------------------------------------
    // Build, patch and invoke helpers
    // --------------------------------------------------------------------------------------------------------

    fun irCallFromBuild(
        buildFun: IrSimpleFunction,
        target: FqName,
        classSymbol: IrClassSymbol? = pluginContext.getSymbol(target)
    ): IrExpression {

        if (classSymbol != null) {
            val constructorCall =
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    classSymbol.defaultType,
                    classSymbol.constructors.single(),
                    typeArgumentsCount = 0,
                    constructorTypeArgumentsCount = 0,
                    Indices.ADAPTIVE_GENERATED_FRAGMENT_ARGUMENT_COUNT
                )

            constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_ADAPTER, irGetValue(irClass.property(Names.ADAPTER), irGet(buildFun.dispatchReceiverParameter !!)))
            constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_PARENT, irGet(buildFun.valueParameters[Indices.BUILD_PARENT]))
            constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_INDEX, irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]))

            return constructorCall
        }

        return irCall(
            pluginContext.adapterActualizeFun,
            irGetValue(irClass.property(Names.ADAPTER), irGet(buildFun.dispatchReceiverParameter !!)),
            irConst(target.asString()),
            irGet(buildFun.valueParameters[Indices.BUILD_PARENT]),
            irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX])
        )
    }

    fun irFragmentFactoryFromPatch(patchFun: IrSimpleFunction, index: Int): IrExpression {
        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                pluginContext.boundFragmentFactoryType,
                pluginContext.boundFragmentFactoryConstructor,
                typeArgumentsCount = 0,
                constructorTypeArgumentsCount = 0,
                Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT
            )

        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT, irGet(patchFun.dispatchReceiverParameter !!))
        constructorCall.putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX, irConst(index))

        return constructorCall
    }

    fun irSetDescendantStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0,
            valueArgumentsCount = Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also { call ->

            call.dispatchReceiver = irGet(patchFun.valueParameters.first())

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                value
            )
        }

    fun irSetStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0,
            valueArgumentsCount = Indices.SET_STATE_VARIABLE_ARGUMENT_COUNT
        ).also { call ->

            call.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )

            call.putValueArgument(
                Indices.SET_STATE_VARIABLE_VALUE,
                value
            )
        }

    fun irGetThisStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int) =
        IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.anyNType,
            pluginContext.getThisClosureVariable,
            0,
            Indices.GET_CLOSURE_VARIABLE_ARGUMENT_COUNT
        ).also {

            it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)

            it.putValueArgument(
                Indices.GET_CLOSURE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )
        }

    fun genInvokeBranch(
        invokeFun: IrSimpleFunction,
        supportFunctionIndex: IrVariable,
        callingFragment: IrVariable,
        arguments: IrVariable,
        armSupportFunctionArgument: ArmSupportFunctionArgument
    ): IrBranch =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irEqual(
                irGet(supportFunctionIndex),
                irConst(armSupportFunctionArgument.supportFunctionIndex)
            ),
            genInvokeBranchBody(invokeFun, callingFragment, arguments, armSupportFunctionArgument)
        )

    private fun genInvokeBranchBody(
        invokeFun: IrSimpleFunction,
        callingFragment: IrVariable,
        arguments: IrVariable,
        armSupportFunctionArgument: ArmSupportFunctionArgument
    ): IrExpression {
        val functionToTransform = (armSupportFunctionArgument.irExpression as IrFunctionExpression).function
        val originalClosure = armSupportFunctionArgument.supportFunctionClosure

        val transformClosure =
            originalClosure + functionToTransform.valueParameters.mapIndexed { indexInState, parameter ->
                ArmSupportStateVariable(
                    armSupportFunctionArgument.armClass,
                    indexInState,
                    originalClosure.size + indexInState,
                    parameter
                )
            }

        return IrBlockImpl(
            functionToTransform.startOffset,
            functionToTransform.endOffset,
            functionToTransform.returnType
        ).apply {
            val transform = SupportFunctionTransform(this@ClassBoundIrBuilder, transformClosure, { irGet(invokeFun.dispatchReceiverParameter !!) }, callingFragment, arguments)

            functionToTransform.body !!.statements.forEach {
                statements += it.transformStatement(transform)
            }
        }
    }

    fun IrExpression.transformCreateStateAccess(
        closure: ArmClosure,
        newParent : IrFunction,
        irGetFragment: () -> IrExpression
    ): IrExpression =
        transform(
            StateAccessTransform(
                this@ClassBoundIrBuilder,
                closure,
                pluginContext.getCreateClosureVariable,
                false,
                newParent,
                irGetFragment
            ),
            null
        )

    fun IrStatement.transformThisStateAccess(
        closure: ArmClosure,
        transformInvoke: Boolean = true,
        newParent : IrFunction,
        irGetFragment: () -> IrExpression
    ): IrExpression =
        transform(
            StateAccessTransform(
                this@ClassBoundIrBuilder,
                closure,
                pluginContext.getThisClosureVariable,
                transformInvoke,
                newParent,
                irGetFragment
            ), null) as IrExpression

    fun ArmDependencies.toDirtyMask(): IrExpression {
        var mask = 0
        this.forEach { mask = mask or (1 shl it.indexInClosure) }
        return irConst(mask)
    }

    fun stateVariableType(variable: ArmStateVariable): IrType =
        if (variable.type.isFunction()) pluginContext.boundSupportFunctionType else variable.type

    // --------------------------------------------------------------------------------------------------------
    // Properties
    // --------------------------------------------------------------------------------------------------------

    /**
     * Adds a property to [irClass].
     */
    fun addIrProperty(
        inName: Name,
        inType: IrType,
        inIsVar: Boolean,
        inInitializer: IrExpression? = null,
        overridden: List<IrPropertySymbol>? = null
    ): IrProperty =
        irClass.addIrProperty(inName, inType, inIsVar, inInitializer, overridden)

}