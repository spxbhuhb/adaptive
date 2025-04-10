/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.FqNames
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmSelectBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class ArmSelect(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    startOffset: Int,
    val subjectSymbol : IrSymbol?,
    val subjectExpression: ArmExpression?
) : ArmRenderingStatement(armClass, index, closure, startOffset) {

    val target = FqNames.ADAPTIVE_SELECT

    val branches = mutableListOf<ArmBranch>()

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmSelectBuilder(parent, this)

}