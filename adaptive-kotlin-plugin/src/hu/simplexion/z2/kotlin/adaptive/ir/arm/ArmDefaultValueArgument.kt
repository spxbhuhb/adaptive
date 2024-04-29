/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmDefaultValueArgumentBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType

/**
 * A function argument that uses the default value provided by the function definition.
 */
class ArmDefaultValueArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    type: IrType,
    value: IrExpression
) : ArmValueArgument(armClass, argumentIndex, type, value, emptyList()) {

    override fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun: IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) =
        ArmDefaultValueArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}