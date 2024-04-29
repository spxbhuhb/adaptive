/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.arm

import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ArmValueArgumentBuilder
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType

open class ArmValueArgument(
    armClass: ArmClass,
    val argumentIndex: Int,
    val type : IrType,
    open val value: IrExpression,
    dependencies: ArmDependencies
) : ArmExpression(armClass, value, dependencies) {

    open fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun: IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) : IrExpression? =
        ArmValueArgumentBuilder(
            classBuilder,
            this, closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}

