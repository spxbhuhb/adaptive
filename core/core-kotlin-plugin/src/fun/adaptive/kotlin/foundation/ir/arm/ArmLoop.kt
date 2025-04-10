/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.FqNames
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmLoopBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder

class ArmLoop(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    startOffset : Int,
    var iterator: ArmDeclaration,
    val body: ArmRenderingStatement
) : ArmRenderingStatement(armClass, index, closure, startOffset) {

    val target = FqNames.ADAPTIVE_LOOP

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmLoopBuilder(parent, this)

}