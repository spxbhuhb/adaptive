/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

class ArmBranch(
    val armClass: ArmClass,
    val condition: ArmExpression,
    val result: ArmRenderingStatement
) : ArmElement {

    val index
        get() = result.index

}