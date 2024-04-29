/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmSequence
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.name.Name

class ArmSequenceBuilder(
    parent: ClassBoundIrBuilder,
    val armSequence: ArmSequence
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun : IrSimpleFunction) : IrExpression =
        irConstructorCallFromBuild(buildFun, armSequence.target)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            Indices.ADAPTIVE_SEQUENCE_ITEM_INDICES,
            IrCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.intArray.defaultType,
                irContext.irBuiltIns.findFunctions(Name.identifier("intArrayOf")).single(),
                0, 1,
            ).apply {
                putValueArgument(0,
                    IrVarargImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        irBuiltIns.intArray.defaultType,
                        irBuiltIns.intType
                    ).apply {
                        elements += armSequence.statements.map { irConst(it.index) }
                    }
                )
            }

        )

}