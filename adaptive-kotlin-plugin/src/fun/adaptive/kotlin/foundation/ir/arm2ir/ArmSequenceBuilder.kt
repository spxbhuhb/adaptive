/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.foundation.Indices
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmDetachExpression
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmSequence
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
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
        irCallFromBuild(buildFun, pluginContext.adapterNewSequenceFun)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
        irIf(
            patchCondition(patchFun, closureMask),
            patchVariableValue(patchFun)
        )

    fun patchCondition(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
        irCall(
            symbol = pluginContext.haveToPatch,
            dispatchReceiver = irGet(patchFun.valueParameters[0]),
            args = arrayOf(
                irGet(closureMask),
                irConst(0) // sequence is hard-coded (for now)
            )
        )

    fun patchVariableValue(patchFun: IrSimpleFunction): IrExpression =
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