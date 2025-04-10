/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.foundation.ir.arm.ArmInternalStateVariable
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmValueProducer
import `fun`.adaptive.kotlin.foundation.ir.util.adatCompanionOrNull
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrSetValueImpl
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.types.typeOrFail
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.isTypeParameter

open class ArmInternalStateVariableBuilder(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmInternalStateVariable,
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
        val transformedInitializer = stateVariable.irVariable.initializer?.transformStateAccess(patchFun)
            ?: irNull()

        + irIf(
            genPatchInternalConditionForMask(patchFun, dirtyMask, stateVariable.dependencies),
            IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType).also { block ->
                block.statements += irSetStateVariable(patchFun, stateVariable.indexInState, transformedInitializer)
                block.statements += irUpdateDirtyMask(dirtyMask)
            }
        )
    }

    fun IrBlockBodyBuilder.genProducePatchInternal(dirtyMask: IrVariable, patchFun: IrSimpleFunction, producer: ArmValueProducer) {
        + irIf(
            genPatchInternalConditionForMask(patchFun, dirtyMask, producer.producerDependencies),
            transformProducer(dirtyMask, producer.producerCall, patchFun).transformStateAccess(patchFun)
        )
        + irIf(
            // the dependencies are the dependencies of the postprocessing parts + the variable itself, see internals in producer.md
            genPatchInternalConditionForMask(patchFun, dirtyMask, stateVariable.dependencies + stateVariable),
            irSetStateVariable(patchFun, stateVariable.indexInState, transformPostProcess(patchFun).transformStateAccess(patchFun))
        )
    }

    fun IrExpression.transformStateAccess(patchFun: IrSimpleFunction) =
        transformThisStateAccess(armClass.stateVariables, newParent = patchFun) { irGet(patchFun.dispatchReceiverParameter !!) }

    /**
     * Set the state variable binding parameter of producer calls.
     */
    fun transformProducer(dirtyMask: IrVariable, producerCall: IrCall, patchFun: IrSimpleFunction): IrExpression {

        for ((index, valueParam) in producerCall.symbol.owner.valueParameters.withIndex()) {
            if (! valueParam.type.isSubtypeOfClass(pluginContext.adaptiveStateVariableBindingClass)) continue
            setBindingParameter(producerCall, index, patchFun)
        }

        // set the dirty mask we use in internal-patch
        // if the producer changes, it produces a new value
        // if there is no post-process, we have to use a dirty mask that
        // contains the produced value

        return IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType).also { block ->
            block.statements += producerCall
            block.statements += irUpdateDirtyMask(dirtyMask)
        }
    }

    private fun setBindingParameter(call: IrCall, index: Int, patchFun: IrSimpleFunction) {

        // if the producer return value is an adat class we have to find the companion
        // object and pass it to `localBinding`

        val returnType = call.symbol.owner.returnType as IrSimpleType
        val producedType : IrType

        if (returnType.isTypeParameter()) {
            val typeArgIndex = call.symbol.owner.typeParameters.indexOfFirst { it.symbol == returnType.classifier }
            val typeArg = call.getTypeArgument(typeArgIndex)
            checkNotNull(typeArg)
            producedType = typeArg
        } else {
            producedType = returnType.typeOrFail
        }

        call.putValueArgument(
            index,
            irCall(
                pluginContext.localBinding,
                dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!),
                args = arrayOf(
                    irConst(stateVariable.indexInState),
                    irConst(producedType.type.classFqName !!.asString()),
                    adatCompanionOrNull(producedType)
                )
            )
        )
    }

    /**
     * Set the dispatch receiver and parameters of `getProducedValue`.
     */
    private fun transformPostProcess(patchFun: IrSimpleFunction): IrExpression =
        ProducerPostProcessTransform(pluginContext, patchFun, stateVariable).visitExpression(stateVariable.irVariable.initializer !!)

    private fun irUpdateDirtyMask(dirtyMask: IrVariable): IrStatement =
        IrSetValueImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            dirtyMask.symbol,
            irOr(
                irGet(dirtyMask),
                irConst(1 shl stateVariable.indexInState)
            ),
            null
        )

}