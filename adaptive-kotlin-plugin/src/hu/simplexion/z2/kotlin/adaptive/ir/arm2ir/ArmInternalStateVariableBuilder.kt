package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmInternalStateVariable
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmValueProducer
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.classFqName

open class ArmInternalStateVariableBuilder(
    parent: ClassBoundIrBuilder,
    val stateVariable: ArmInternalStateVariable
) : ClassBoundIrBuilder(parent) {

    fun genInitializer(internalPatchFun: IrSimpleFunction): IrExpression {
        val producer = stateVariable.producer

        if (producer == null) {
            return stateVariable.irVariable.initializer!!
        } else {
            return genBinding(internalPatchFun, producer)
        }
    }

    fun genBinding(internalPatchFun: IrSimpleFunction, producer: ArmValueProducer) : IrExpression {

        val call = stateVariable.irVariable.initializer as IrCall

        call.putValueArgument(
            producer.argumentIndex - 1,
            irCall(
                pluginContext.localBinding,
                dispatchReceiver = irGet(internalPatchFun.dispatchReceiverParameter!!),
                args = arrayOf(
                    irConst(stateVariable.indexInState),
                    irConst(producer.supportFunctionIndex),
                    irConst(stateVariable.type.classFqName !!.asString())
                )
            )
        )

        call.putValueArgument(producer.argumentIndex, irNull())

        return call
    }

}