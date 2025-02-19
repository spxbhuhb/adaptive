/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.foundation.Indices
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClass
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmDependencies
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

open class ClassBoundIrBuilder(
    override val pluginContext: FoundationPluginContext,
    val armClass: ArmClass
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
        target: IrSimpleFunctionSymbol
    ) =
        irCall(
            target,
            irGetValue(pluginContext.adapter, irGet(buildFun.valueParameters[Indices.BUILD_PARENT])),
            irGet(buildFun.valueParameters[Indices.BUILD_PARENT]),
            irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX])
        )

    /**
     * Called by `ArmLoopBuilder`. As of now we support only `for () { }` style loops
     * which cannot have function references, so passing null as the factory function
     * reference is fine.
     */
    fun irFragmentFactoryFromPatch(patchFun: IrSimpleFunction, index: Int): IrExpression {
        val constructorCall =
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                pluginContext.boundFragmentFactoryType,
                pluginContext.boundFragmentFactoryConstructor,
                typeArgumentsCount = 0,
                constructorTypeArgumentsCount = 0
            ).also {
                it.putValueArgument(0, irGet(patchFun.dispatchReceiverParameter !!))
                it.putValueArgument(1, irConst(index))
                it.putValueArgument(2, irNull())
            }

        return constructorCall
    }

    fun irSetDescendantStateVariable(patchFun: IrSimpleFunction, stateVariableIndex: Int, value: IrExpression) =
        IrCallImpl(
            SYNTHETIC_OFFSET,
            SYNTHETIC_OFFSET,
            irBuiltIns.unitType,
            pluginContext.setStateVariable,
            typeArgumentsCount = 0
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
            typeArgumentsCount = 0
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
            typeArgumentsCount = 0
        ).also {

            it.dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!)

            it.putValueArgument(
                Indices.GET_CLOSURE_VARIABLE_INDEX,
                irConst(stateVariableIndex)
            )
        }

    fun genPatchInternalConditionForMask(patchFun: IrSimpleFunction, dirtyMask: IrVariable, dependencies: ArmDependencies): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!),
            args = arrayOf(
                irGet(dirtyMask),
                dependencies.toDirtyMask()
            )
        )

    fun IrExpression.transformCreateStateAccess(
        closure: ArmClosure,
        newParent: IrFunction,
        irGetFragment: () -> IrExpression
    ): IrExpression =
        transform(
            StateAccessTransform(
                this@ClassBoundIrBuilder,
                closure,
                pluginContext.getCreateClosureVariable,
                newParent,
                irGetFragment
            ),
            null
        )

    fun IrStatement.transformThisStateAccess(
        closure: ArmClosure,
        newParent: IrFunction,
        irGetFragment: () -> IrExpression
    ): IrExpression =
        transform(
            StateAccessTransform(
                this@ClassBoundIrBuilder,
                closure,
                pluginContext.getThisClosureVariable,
                newParent,
                irGetFragment
            ), null
        ) as IrExpression

    fun ArmDependencies.toDirtyMask(): IrExpression {
        var mask = 0
        this.forEach { mask = mask or (1 shl it.indexInClosure) }
        return irConst(mask)
    }

}