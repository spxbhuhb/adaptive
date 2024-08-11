/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import org.jetbrains.kotlin.ir.expressions.IrExpression

open class ArmExpression(
    val armClass: ArmClass,
    open val irExpression: IrExpression,
    val dependencies: ArmDependencies
) : ArmElement