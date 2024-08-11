/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmStateVariableBindingArgumentBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.types.IrType

/**
 * A function argument that is an AdaptiveStateVariableBinding.
 */
class ArmStateVariableBindingArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val indexInState: Int,
    val indexInClosure: Int,
    val boundType: IrType,
    val supportFunctionIndex: Int,
    val path : List<String>,
    type : IrType,
    value: IrFunctionExpression,
    dependencies: ArmDependencies,
) : ArmValueArgument(armClass, argumentIndex, type, value, dependencies) {

    override fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun : IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) =
        ArmStateVariableBindingArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}