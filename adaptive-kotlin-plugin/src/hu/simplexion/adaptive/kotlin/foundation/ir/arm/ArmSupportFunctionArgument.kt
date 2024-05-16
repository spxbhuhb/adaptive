/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ArmSupportFunctionArgumentBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.types.IrType

/**
 * A function argument that is a function itself, but not an adaptive one.
 */
class ArmSupportFunctionArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val supportFunctionIndex: Int,
    val supportFunctionClosure: ArmClosure,
    type : IrType,
    override val value: IrFunctionExpression,
    dependencies: ArmDependencies,
) : ArmValueArgument(armClass, argumentIndex, type, value, dependencies) {

    val isSuspend
        get() = value.function.isSuspend

    override fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun : IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) =
        ArmSupportFunctionArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}