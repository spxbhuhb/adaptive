/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.arm

import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ArmValueProducerBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression

open class ArmValueProducer(
    val armClass: ArmClass,
    val argumentIndex: Int, // argument index of the support function
    val supportFunctionIndex: Int,
    var irExpression: IrFunctionExpression,
    val dependencies: ArmDependencies
) : ArmElement {

    val isSuspend
        get() = irExpression.function.isSuspend

    fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmValueProducerBuilder(parent, this)

}