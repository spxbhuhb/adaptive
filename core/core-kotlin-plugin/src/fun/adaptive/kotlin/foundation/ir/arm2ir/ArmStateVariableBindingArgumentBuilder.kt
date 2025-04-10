/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmStateVariableBindingArgument
import `fun`.adaptive.kotlin.foundation.ir.util.adatCompanionOrNull
import `fun`.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

class ArmStateVariableBindingArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmStateVariableBindingArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun patchVariableValue(patchFun: IrSimpleFunction): IrExpression =
        irCall(
            pluginContext.setBinding,
            dispatchReceiver = irGet(patchFun.dispatchReceiverParameter !!),
            args = arrayOf(
                irConst(argument.indexInState), // indexInThis
                irGet(patchFun.valueParameters.first()), // descendant
                irConst(argument.argumentIndex), // indexInTarget
                genPath(argument), // path
                irConst(Signature.typeSignature(argument.boundType, pluginContext.adatClass)), // boundType
                adatCompanionOrNull(closure[argument.indexInClosure].type)
            )
        )

    private fun genPath(argument: ArmStateVariableBindingArgument): IrExpression {
        if (argument.path.isEmpty()) {
            return irNull()
        }

        return irArrayOf(argument.path.reversed().map { irConst(it) })
    }
}
