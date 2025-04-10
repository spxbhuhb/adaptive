/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder

abstract class ArmRenderingStatement(
    val armClass: ArmClass,
    val index: Int,
    val closure : ArmClosure,
    val startOffset: Int
) : ArmElement {

    abstract fun branchBuilder(parent: ClassBoundIrBuilder) : BranchBuilder

}