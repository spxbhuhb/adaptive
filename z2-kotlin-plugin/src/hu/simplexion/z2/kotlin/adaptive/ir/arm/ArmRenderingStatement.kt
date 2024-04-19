/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.BranchBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ClassBoundIrBuilder

abstract class ArmRenderingStatement(
    val armClass: ArmClass,
    val index: Int,
    val closure : ArmClosure,
    val startOffset: Int
) : ArmElement {

    abstract fun branchBuilder(parent: ClassBoundIrBuilder) : BranchBuilder

    var hasInvokeBranch : Boolean = false
    var hasInvokeSuspendBranch : Boolean = false
    var transforms : MutableList<ArmTransformCall>? = null

}