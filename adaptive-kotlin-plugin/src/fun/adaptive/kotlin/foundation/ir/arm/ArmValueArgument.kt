/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmValueArgumentBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType

open class ArmValueArgument(
    armClass: ArmClass,
    val argumentIndex: Int,
    val type : IrType,
    override val irExpression: IrExpression,
    dependencies: ArmDependencies,
    val detachExpressions : List<ArmDetachExpression> = emptyList(),
    val isInstructions : Boolean = false,
) : ArmExpression(armClass, irExpression, dependencies) {

    open fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun: IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) : IrExpression? =
        ArmValueArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}

