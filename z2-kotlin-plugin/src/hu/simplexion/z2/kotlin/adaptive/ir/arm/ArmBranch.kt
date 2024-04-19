/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

class ArmBranch(
    val armClass: ArmClass,
    val condition: ArmExpression,
    val result: ArmRenderingStatement
) : ArmElement {

    val index
        get() = result.index

}