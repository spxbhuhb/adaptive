package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmDefaultValueArgument
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

class ArmDefaultValueArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmDefaultValueArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun genPatchDescendantExpression(patchFun : IrSimpleFunction): IrExpression? =
        null

}