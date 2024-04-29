/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.arm

import hu.simplexion.adaptive.kotlin.base.FqNames
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ArmSequenceBuilder
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.BranchBuilder
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ClassBoundIrBuilder

class ArmSequence(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    startOffset: Int,
    val statements : List<ArmRenderingStatement>,
) : ArmRenderingStatement(armClass, index, closure, startOffset) {

    val target = FqNames.ADAPTIVE_SEQUENCE

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmSequenceBuilder(parent, this)

}