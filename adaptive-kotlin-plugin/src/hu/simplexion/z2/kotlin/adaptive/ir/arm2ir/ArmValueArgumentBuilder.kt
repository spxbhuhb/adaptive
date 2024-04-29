package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmValueArgument
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

open class ArmValueArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val valueArgument: ArmValueArgument,
    val closure: ArmClosure,
    val fragment: IrValueParameter,
    val closureDirtyMask: IrVariable
) : ClassBoundIrBuilder(parent) {

    open fun genPatchDescendantExpression(patchFun : IrSimpleFunction): IrExpression? =
        irIf(
            patchCondition(),
            patchBody(patchFun)
        )

    fun patchCondition(): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(fragment),
            args = arrayOf(
                irGet(closureDirtyMask),
                valueArgument.dependencies.toDirtyMask()
            )
        )

    open fun patchBody(patchFun : IrSimpleFunction): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            valueArgument.argumentIndex,
            valueArgument.irExpression.transformCreateStateAccess(closure) { irGet(fragment) }
        )
}