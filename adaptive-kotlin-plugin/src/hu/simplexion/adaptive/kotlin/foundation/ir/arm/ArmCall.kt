/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ArmCallBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.expressions.IrCall

open class ArmCall(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    val isDirect: Boolean,
    val irCall: IrCall,
    val isExpectCall : Boolean
) : ArmRenderingStatement(armClass, index, closure, irCall.startOffset) {

    val target = irCall.symbol.owner.adaptiveClassFqName()

    val arguments = mutableListOf<ArmValueArgument>()

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmCallBuilder(parent, this)

}