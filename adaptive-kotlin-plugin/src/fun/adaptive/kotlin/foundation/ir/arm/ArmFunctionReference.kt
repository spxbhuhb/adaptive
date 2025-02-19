/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.FqNames
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmCallBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmFunctionReferenceBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import `fun`.adaptive.kotlin.foundation.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.getAnnotationStringValue

@OptIn(UnsafeDuringIrConstructionAPI::class)
open class ArmFunctionReference(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    irExpression: IrExpression,
) : ArmRenderingStatement(armClass, index, closure, irExpression.startOffset) {

    val arguments = mutableListOf<ArmValueArgument>()

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmFunctionReferenceBuilder(parent, this)

}