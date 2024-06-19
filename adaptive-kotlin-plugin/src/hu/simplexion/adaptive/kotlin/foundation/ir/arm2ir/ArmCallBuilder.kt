/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.Strings
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmCall
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getPropertyGetter

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
                    irGetValue(pluginContext.adapter, irGet(buildFun.valueParameters[Indices.BUILD_PARENT])),
                    irConst(armCall.getExpectName()),
                    irGet(buildFun.valueParameters[Indices.BUILD_PARENT]),
                    irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX])
                )
            }

            armCall.isDirect -> {
                // FIXME remove fixValueParameters as soon as KT-58886 is solved, it's really hackish
                fixValueParameters(armCall.irCall.symbol.owner)
                irCall(
                    armCall.irCall.symbol,
                    dispatchReceiver = null,
                    irGet(buildFun.valueParameters[Indices.BUILD_PARENT]),
                    irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX])
                )
            }

            else -> {
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    pluginContext.adaptiveAnonymousClass.defaultType,
                    pluginContext.anonymousConstructor,
                    typeArgumentsCount = 0,
                    constructorTypeArgumentsCount = 0,
                    valueArgumentsCount = 4
                ).also {
                    it.putValueArgument(0, irGet(buildFun.valueParameters[Indices.BUILD_PARENT]))
                    it.putValueArgument(1, irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX]))
                    it.putValueArgument(2, irConst(armCall.arguments.count()))
                    it.putValueArgument(3, irGetFragmentFactory(buildFun))
                }
            }
        }

    private fun fixValueParameters(owner: IrSimpleFunction) {
        if (owner.valueParameters.size == 2
            && owner.valueParameters[0].type == pluginContext.adaptiveFragmentType
            && owner.valueParameters[1].type == irBuiltIns.intType
        ) return

        owner.valueParameters = emptyList()
        owner.addValueParameter(Strings.PARENT, pluginContext.adaptiveFragmentType)
        owner.addValueParameter(Strings.DECLARATION_INDEX, irBuiltIns.intType)
    }

    fun irGetFragmentFactory(buildFun: IrSimpleFunction): IrExpression {

        val valueParameter = (armCall.irCall.dispatchReceiver as IrGetValue).symbol.owner as IrValueParameterImpl
        val argumentIndex = valueParameter.index

        val getState = irCall(
            irClass.getPropertyGetter(Strings.STATE) !!,
            dispatchReceiver = irGet(buildFun.dispatchReceiverParameter !!)
        )

        val getStateVariable = irCall(
            pluginContext.arrayGet,
            dispatchReceiver = getState,
            args = arrayOf(irConst(argumentIndex))
        )

        return irImplicitAs(
            pluginContext.boundFragmentFactoryClass.defaultType,
            getStateVariable
        )
    }

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrBlock {
        val fragmentParameter = patchFun.valueParameters.first()

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
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

                armCall.transforms?.forEach { transform ->
                    block.statements += transform.irCall
                        .also {
                            it.dispatchReceiver =
                                irImplicitAs(
                                    it.dispatchReceiver !!.type,
                                    irGet(patchFun.valueParameters.first())
                                )
                        }
                        .transformCreateStateAccess(armCall.closure, patchFun) { irGet(patchFun.dispatchReceiverParameter !!) }
                }
            }
    }

}