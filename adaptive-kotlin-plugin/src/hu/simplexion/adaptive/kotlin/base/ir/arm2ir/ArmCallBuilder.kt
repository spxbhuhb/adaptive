/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.base.ir.arm2ir

import hu.simplexion.adaptive.kotlin.base.Indices
import hu.simplexion.adaptive.kotlin.base.Names
import hu.simplexion.adaptive.kotlin.base.Strings
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmCall
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmSupportFunctionArgument
import hu.simplexion.adaptive.kotlin.common.property
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getPropertyGetter

class ArmCallBuilder(
    parent: ClassBoundIrBuilder,
    val armCall: ArmCall
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun: IrSimpleFunction): IrExpression =
        when {
            armCall.isExpectCall -> {
                irCall(
                    pluginContext.adapterActualizeFun,
                    irGetValue(irClass.property(Names.ADAPTER), irGet(buildFun.dispatchReceiverParameter !!)),
                    irConst(armCall.target.asString()),
                    irGet(buildFun.valueParameters[Indices.BUILD_PARENT]),
                    irGet(buildFun.valueParameters[Indices.BUILD_DECLARATION_INDEX])
                )
            }

            armCall.isDirect -> {
                irCallFromBuild(buildFun, armCall.target)
            }

            else -> {
                irConstructorCallFromBuild(
                    buildFun,
                    pluginContext.adaptiveAnonymousClass,
                    argumentCount = Indices.ADAPTIVE_ANONYMOUS_FRAGMENT_ARGUMENT_COUNT
                ).apply {
                    putValueArgument(Indices.ADAPTIVE_FRAGMENT_STATE_SIZE, irConst(armCall.arguments.count()))
                    putValueArgument(Indices.ADAPTIVE_FRAGMENT_FACTORY, irGetFragmentFactory(buildFun))
                }
            }
        }

    fun irGetFragmentFactory(buildFun: IrSimpleFunction): IrExpression {

        val valueParameter = (armCall.irCall.dispatchReceiver as IrGetValue).symbol.owner as IrValueParameterImpl
        val argumentIndex = valueParameter.index

        val getState = irCall(
            irClass.getPropertyGetter(Strings.STATE)!!,
            dispatchReceiver = irGet(buildFun.dispatchReceiverParameter!!)
        )

        val getStateVariable = irCall(
            pluginContext.arrayGet,
            dispatchReceiver = getState,
            args = arrayOf(irConst(argumentIndex))
        )

        return irImplicitAs(
            pluginContext.adaptiveFragmentFactoryClass.defaultType,
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
                                    it.dispatchReceiver!!.type,
                                    irGet(patchFun.valueParameters.first())
                                )
                        }
                        .transformCreateStateAccess(armCall.closure) { irGet(patchFun.dispatchReceiverParameter!!) }
                }
            }
    }

    override fun genInvokeBranches(
        invokeFun: IrSimpleFunction,
        supportFunctionIndex: IrVariable,
        callingFragment: IrVariable,
        arguments: IrVariable
    ): List<IrBranch> =
        armCall.arguments
            .filterIsInstance<ArmSupportFunctionArgument>()
            .map { genInvokeBranch(invokeFun, supportFunctionIndex, callingFragment, arguments, it) }

}