/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.FqNames
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmSequenceBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.BranchBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder

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