/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.common.firstRegularParameter
import `fun`.adaptive.kotlin.common.regularParameterCount
import `fun`.adaptive.kotlin.common.secondRegularParameter
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmCall
import `fun`.adaptive.kotlin.foundation.ir.util.argumentCallGetValue
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.ir.util.isSubtypeOfClass

@OptIn(UnsafeDuringIrConstructionAPI::class)
class ArmCallBuilder(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun: IrSimpleFunction): IrExpression =
        when {
            armCall.isExpectCall -> {
                irCall(
                    pluginContext.adapterActualizeFun,
                    irGetValue(pluginContext.adapter, irGet(buildFun.firstRegularParameter)),
                    irConst(armCall.getExpectName()),
                    irGet(buildFun.firstRegularParameter),
                    irGet(buildFun.secondRegularParameter),
                    irConst(armCall.arguments.count())
                )
            }

            armCall.isDirect -> {
                // FIXME remove fixValueParameters as soon as KT-58886 is solved, it's really hackish
                fixFunSignature(armCall.irCall.symbol.owner)
                irCall(
                    armCall.irCall.symbol,
                    dispatchReceiver = null,
                    irGet(buildFun.firstRegularParameter),
                    irGet(buildFun.secondRegularParameter)
                )
            }

            else -> {
                // AdaptiveAnonymous(parent = parent, index = declarationIndex, stateSize = 0, factory = <this>.<get-state>().get(index = -1) /*as BoundFragmentFactory */)
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    pluginContext.adaptiveAnonymousClass.defaultType,
                    pluginContext.anonymousConstructor,
                    typeArgumentsCount = 0,
                    constructorTypeArgumentsCount = 0
                ).also {
                    it.arguments[0] = irGet(buildFun.firstRegularParameter)
                    it.arguments[1] = irGet(buildFun.secondRegularParameter)
                    it.arguments[2] = irConst(armCall.arguments.count())
                    it.arguments[3] =  irGetFragmentFactory(buildFun)
                }
            }
        }

    private fun fixFunSignature(owner: IrSimpleFunction) {
        if (owner.regularParameterCount == 2
            && owner.firstRegularParameter.type == pluginContext.adaptiveFragmentType
            && owner.secondRegularParameter.type == irBuiltIns.intType
        ) return

        owner.parameters = emptyList()
        owner.addValueParameter(Strings.PARENT, pluginContext.adaptiveFragmentType)
        owner.addValueParameter(Strings.DECLARATION_INDEX, irBuiltIns.intType)

        if (! owner.returnType.isSubtypeOfClass(pluginContext.adaptiveFragmentClass)) {
            owner.returnType = pluginContext.adaptiveFragmentType
        }
    }

    /**
     * Get the fragment factory from the state of the fragment.
     *
     * Original source code:
     *
     * ```kotlin
     * @Adaptive
     * fun inner(@Adaptive builder: () -> Unit) {
     *     builder()
     * }
     * ```
     *
     * Generated code for `builder()` that uses this function (the `factory` parameter):
     *
     * ```kotlin
     * AdaptiveAnonymous(parent = parent, index = declarationIndex, stateSize = 0, factory = <this>.<get-state>().get(index = 1) /*as BoundFragmentFactory */)
     * ```
     *
     * `(armCall.irCall.dispatchReceiver as IrGetValue).symbol.owner` is the `builder` value parameter of `inner`
     */
    fun irGetFragmentFactory(buildFun: IrSimpleFunction): IrExpression {

        val getState = irCall(
            irClass.getPropertyGetter(Strings.STATE) !!,
            dispatchReceiver = irGet(buildFun.dispatchReceiverParameter !!)
        )

        val getStateVariable = irCall(
            pluginContext.arrayGet,
            dispatchReceiver = getState,
            args = arrayOf(irConst(argumentIndex()))
        )

        return irImplicitAs(
            pluginContext.boundFragmentFactoryClass.defaultType,
            getStateVariable
        )
    }

    fun argumentIndex() : Int {
        val get = checkNotNull(armCall.irCall.argumentCallGetValue) { "cannot find argument for call"}
        val valueParameter = get.symbol.owner as IrValueParameterImpl
        return armClass.stateVariables.indexOfFirst { it.name == valueParameter.name.asString() }
    }

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression {
        val fragmentParameter = patchFun.firstRegularParameter

        IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->

                for (argument in armCall.arguments) {
                    argument.toPatchExpression(
                        this,
                        patchFun,
                        armCall.closure,
                        fragmentParameter,
                        closureMask
                    )?.also {
                        block.statements += it
                    }
                }

                if (! armCall.isExpectCall && ! armCall.isDirect) {
                    block.statements += genPatchDescendantAnonymousFactory(patchFun, closureMask, fragmentParameter)
                }

                return block
            }
    }

    fun genPatchDescendantAnonymousFactory(patchFun: IrSimpleFunction, closureMask: IrVariable, fragmentParameter : IrValueParameter): IrExpression =
        irIf(
            irCall(
                symbol = pluginContext.haveToPatch,
                dispatchReceiver = irGet(fragmentParameter),
                args = arrayOf(
                    irGet(closureMask),
                    listOf(armClass.stateVariables[argumentIndex()]).toDirtyMask()
                )
            ),
            irCall(
                pluginContext.anonymousFactorySetterSymbol,
                irGet(fragmentParameter),
                irGetFragmentFactory(patchFun)
            )
        )

}