/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.base.ir.arm2ir

import hu.simplexion.adaptive.kotlin.base.Indices
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmBranch
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmClosure
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmSelect
import hu.simplexion.adaptive.kotlin.base.ir.arm.ArmWhenStateVariable
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irComposite
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrElseBranchImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrWhenImpl
import org.jetbrains.kotlin.ir.symbols.IrVariableSymbol
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmSelectBuilder(
    parent: ClassBoundIrBuilder,
    val armSelect: ArmSelect
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun: IrSimpleFunction): IrExpression =
        irConstructorCallFromBuild(buildFun, armSelect.target)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            Indices.ADAPTIVE_SELECT_BRANCH,
            if (armSelect.subjectSymbol == null) {
                irSelectWhenNoSubject(patchFun)
            } else {
                irSelectWhenWithSubject(patchFun)
            }
        )

    private fun irSelectWhenNoSubject(patchFun: IrSimpleFunction): IrExpression =
        IrWhenImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irBuiltIns.intType,
            IrStatementOrigin.WHEN
        ).apply {

            armSelect.branches.forEach { branch ->
                branches += irConditionBranchNoSubject(patchFun, branch)
            }

            // add "else" if the last condition is not a constant true
            armSelect.branches.last().condition.irExpression.let {
                if (! (it is IrConst<*> && it.value is Boolean && it.value == true)) {
                    branches += irElseBranch()
                }
            }
        }

    private fun irConditionBranchNoSubject(patchFun: IrSimpleFunction, branch: ArmBranch) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            branch.condition.irExpression.transformCreateStateAccess(armSelect.closure) { irGet(patchFun.valueParameters.first()) },
            irConst(branch.index)
        )

    private fun irSelectWhenWithSubject(patchFun: IrSimpleFunction): IrExpression {
        val subject = armSelect.subjectExpression !!.irExpression
        val subjectName = (armSelect.subjectSymbol as IrVariableSymbol).owner.name

        return DeclarationIrBuilder(irContext, patchFun.symbol).irComposite(subject, resultType = subject.type) {

            val subjectVariable = irTemporary(
                subject.transformCreateStateAccess(armSelect.closure) { irGet(patchFun.valueParameters.first()) },
            )

            val transformClosure =
                armSelect.closure + ArmWhenStateVariable(armSelect.armClass, 0, armSelect.closure.size, subjectName.identifier, subjectVariable)

            + IrWhenImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                irBuiltIns.intType,
                IrStatementOrigin.WHEN
            ).apply {

                armSelect.branches.forEach { branch ->
                    branches += irConditionBranchWithSubject(patchFun, branch, transformClosure)
                }

                // add "else" if the last condition is not a constant true
                armSelect.branches.last().condition.irExpression.let {
                    if (! (it is IrConst<*> && it.value is Boolean && it.value == true)) {
                        branches += irElseBranch()
                    }
                }
            }
        }
    }

    private fun irConditionBranchWithSubject(patchFun: IrSimpleFunction, branch: ArmBranch, transformClosure: ArmClosure) =
        IrBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            branch.condition.irExpression.transformCreateStateAccess(transformClosure) { irGet(patchFun.valueParameters.first()) },
            irConst(branch.index)
        )

    private fun irElseBranch() =
        IrElseBranchImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            irConst(true),
            irConst(- 1)
        )

}