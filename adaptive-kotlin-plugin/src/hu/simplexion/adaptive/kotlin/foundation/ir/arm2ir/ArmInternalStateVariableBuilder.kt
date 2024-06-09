/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmInternalStateVariable
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmValueProducer
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

open class ArmInternalStateVariableBuilder(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmInternalStateVariable
) : ClassBoundIrBuilder(parent) {

    fun genPatchInternal(irBlockBodyBuilder: IrBlockBodyBuilder, dirtyMask: IrVariable, patchFun: IrSimpleFunction) {
        val producer = stateVariable.producer

        with(irBlockBodyBuilder) {
            if (producer == null) {
                genNormalPatchInternal(dirtyMask, patchFun)
            } else {
                genProducePatchInternal(dirtyMask, patchFun, producer)
            }
        }
    }

    fun IrBlockBodyBuilder.genNormalPatchInternal(dirtyMask: IrVariable, patchFun: IrSimpleFunction) {
        val transformedInitializer = stateVariable.irVariable.initializer!!.transformStateAccess(patchFun)

        + irIf(
            genPatchInternalConditionForMask(patchFun, dirtyMask, stateVariable.dependencies),
            irSetStateVariable(patchFun, stateVariable.indexInState, transformedInitializer)
        )
    }

    fun IrBlockBodyBuilder.genProducePatchInternal(dirtyMask: IrVariable, patchFun: IrSimpleFunction, producer: ArmValueProducer) {
        + irIf(
            genPatchInternalConditionForMask(patchFun, dirtyMask, producer.producerDependencies),
            transformProducer(producer.producerCall, patchFun).transformStateAccess(patchFun)
        )
        + irIf(
            // the dependencies are the dependencies of the postprocessing parts + the variable itself, see internals in producer.md
            genPatchInternalConditionForMask(patchFun, dirtyMask, stateVariable.dependencies + stateVariable),
            irSetStateVariable(patchFun, stateVariable.indexInState, transformPostProcess(patchFun).transformStateAccess(patchFun))
        )
    }

    fun IrExpression.transformStateAccess(patchFun: IrSimpleFunction) =
        transformThisStateAccess(armClass.stateVariables, newParent = patchFun, stateVariable = stateVariable) { irGet(patchFun.dispatchReceiverParameter !!) }

    /**
     * Set the state variable binding parameter of producer calls.
     */
    fun transformProducer(expression: IrCall, patchFun: IrSimpleFunction) : IrExpression {
        for ((index, valueParam) in expression.symbol.owner.valueParameters.withIndex()) {
            if ( ! valueParam.type.isSubtypeOfClass(pluginContext.adaptiveStateVariableBindingClass)) continue
            expression.putValueArgument(
                index,
                irCall(
                    pluginContext.localBinding,
                    dispatchReceiver = irGet(patchFun.dispatchReceiverParameter!!),
                    args = arrayOf(
                        irConst(stateVariable.indexInState),
                        irConst(stateVariable.type.classFqName !!.asString())
                    )
                )
            )
        }
        return expression
    }

    /**
     * Set the dispatch receiver and parameters of `getProducedValue`.
     */
    private fun transformPostProcess(patchFun: IrSimpleFunction): IrExpression =
        ProducerPostProcessTransform(pluginContext, patchFun, stateVariable).visitExpression(stateVariable.irVariable.initializer!!)

}