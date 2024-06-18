/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ArmFragmentFactoryArgumentBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.types.IrType

class ArmFragmentFactoryArgument(
    armClass: ArmClass,
    argumentIndex: Int,
    val fragmentIndex: Int,
    val closure: ArmClosure,
    type : IrType,
    override val irExpression: IrFunctionExpression,
    dependencies: ArmDependencies
) : ArmValueArgument(
    armClass,
    argumentIndex,
    type,
    irExpression,
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