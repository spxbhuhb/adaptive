package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmLoop
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
        irConstructorCallFromBuild(buildFun, armLoop.target)

    override fun genPatchDescendantBranch(patchFun: IrSimpleFunction, closureMask: IrVariable): IrExpression =
         IrBlockImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, pluginContext.irContext.irBuiltIns.unitType)
            .also { block ->

                 val iteratorInitializer = ((armLoop.iterator.irDeclaration as IrVariable).initializer!!)

                 block.statements += irSetDescendantStateVariable(
                     patchFun,
                     Indices.ADAPTIVE_LOOP_ITERATOR,
                     iteratorInitializer.transformThisStateAccess(armLoop.closure, transformInvoke = false) { irGet(patchFun.dispatchReceiverParameter !!) }
                 )

                 block.statements += irSetDescendantStateVariable(
                     patchFun,
                     Indices.ADAPTIVE_LOOP_FACTORY,
                     irFragmentFactoryFromPatch(patchFun, armLoop.body.index)
                 )

            }


}