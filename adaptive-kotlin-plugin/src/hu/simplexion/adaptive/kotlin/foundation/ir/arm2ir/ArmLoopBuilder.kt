/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.foundation.ir.arm2ir

import hu.simplexion.adaptive.kotlin.foundation.Indices
import hu.simplexion.adaptive.kotlin.foundation.ir.arm.ArmLoop
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET

class ArmLoopBuilder(
    parent: ClassBoundIrBuilder,
    val armLoop: ArmLoop
) : ClassBoundIrBuilder(parent), BranchBuilder {

    override fun genBuildConstructorCall(buildFun : IrSimpleFunction) : IrExpression =
        irCallFromBuild(buildFun, pluginContext.adapterNewLoopFun)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
         IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->

                 val iteratorInitializer = ((armLoop.iterator.irDeclaration as IrVariable).initializer!!)

                 block.statements += irSetDescendantStateVariable(
                     patchFun,
                     Indices.ADAPTIVE_LOOP_ITERATOR,
                     iteratorInitializer.transformThisStateAccess(
                         armLoop.closure,
                         newParent = patchFun,
                         transformInvoke = false
                     ) { irGet(patchFun.dispatchReceiverParameter !!) }
                 )

                 block.statements += irSetDescendantStateVariable(
                     patchFun,
                     Indices.ADAPTIVE_LOOP_FACTORY,
                     irFragmentFactoryFromPatch(patchFun, armLoop.body.index)
                 )

            }


}