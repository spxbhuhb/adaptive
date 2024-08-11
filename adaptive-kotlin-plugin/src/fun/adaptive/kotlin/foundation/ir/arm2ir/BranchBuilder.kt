/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression

interface BranchBuilder {

    fun genBuildConstructorCall(buildFun: IrSimpleFunction): IrExpression {
        throw IllegalStateException("should not be called, this is a plugin error")
    }

    fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression {
        throw IllegalStateException("should not be called, this is a plugin error")
    }

}