package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmStateVariableBindingArgument
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmStateVariableBindingArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmStateVariableBindingArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun patchBody(patchFun: IrSimpleFunction): IrExpression =
        irCall(
            pluginContext.setBinding,
            dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!),
            args = arrayOf(
                irConst(argument.indexInState), // indexInThis
                irGet(patchFun.valueParameters.first()), // descendant
                irConst(argument.argumentIndex), // indexInTarget
                genPath(argument), // path
                irConst(argument.boundType.classFqName !!.asString()), // boundType
            )
        )

    private fun genPath(argument: ArmStateVariableBindingArgument): IrExpression {
        if (argument.path.isEmpty()) {
            return irNull()
        }

        val type = irBuiltIns.arrayClass.typeWith(irBuiltIns.stringType)

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            type,
            irBuiltIns.arrayOf,
            1, 1,
        ).apply {
            putTypeArgument(0, irBuiltIns.stringType)
            putValueArgument(0,
                IrVarargImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    type,
                    irBuiltIns.stringType
                ).apply {
                    elements += argument.path.map { irConst(it) }
                }
            )
        }
    }
}
