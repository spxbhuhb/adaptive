/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

open class ArmValueProducer(
    val armClass: ArmClass,
    val argumentIndex: Int, // argument index of the support function
    val supportFunctionIndex: Int,
    var irExpression: IrFunctionExpression,
    val dependencies: ArmDependencies
) : ArmElement