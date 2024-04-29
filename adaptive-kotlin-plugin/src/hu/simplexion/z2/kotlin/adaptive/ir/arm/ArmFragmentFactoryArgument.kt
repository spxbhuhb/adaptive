/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmFragmentFactoryArgumentBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType

class ArmFragmentFactoryArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val fragmentIndex: Int,
    val closure: ArmClosure,
    type : IrType,
    value: IrExpression,
    dependencies: ArmDependencies
) : ArmValueArgument(
    armClass,
    argumentIndex,
    type,
    value,
    dependencies
) {

    override fun toPatchExpression(
        classBuilder: ClassBoundIrBuilder,
        patchFun: IrSimpleFunction,
        closure: ArmClosure,
        fragmentParameter: IrValueParameter,
        closureDirtyMask: IrVariable
    ) =
        ArmFragmentFactoryArgumentBuilder(
            classBuilder,
            this,
            closure,
            fragmentParameter,
            closureDirtyMask
        ).genPatchDescendantExpression(patchFun)

}