/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.arm

import `fun`.adaptive.kotlin.foundation.FqNames
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmCallBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.BranchBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import `fun`.adaptive.kotlin.foundation.ir.util.adaptiveClassFqName
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.getAnnotationStringValue

@OptIn(UnsafeDuringIrConstructionAPI::class)
open class ArmCall(
    armClass: ArmClass,
    index: Int,
    closure: ArmClosure,
    val isDirect: Boolean,
    val irCall: IrCall,
    val isExpectCall: Boolean
) : ArmRenderingStatement(armClass, index, closure, irCall.startOffset) {

    val target = irCall.symbol.owner.adaptiveClassFqName()

    val arguments = mutableListOf<ArmValueArgument>()

    fun getExpectName(): String =
        checkNotNull(irCall.symbol.owner.getAnnotation(FqNames.ADAPTIVE_EXPECT)) { "missing ${Strings.ADAPTIVE_EXPECT} annotation" }
            .getAnnotationStringValue() + ":" + target.shortName().identifier.removePrefix("Adaptive").lowercase()

    override fun branchBuilder(parent: ClassBoundIrBuilder): BranchBuilder =
        ArmCallBuilder(parent, this)

}