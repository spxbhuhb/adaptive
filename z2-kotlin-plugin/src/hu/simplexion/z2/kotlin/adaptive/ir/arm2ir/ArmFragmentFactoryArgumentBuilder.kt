package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmFragmentFactoryArgument
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors

class ArmFragmentFactoryArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmFragmentFactoryArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun patchBody(patchFun: IrSimpleFunction): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            argument.argumentIndex,
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                classBoundFragmentFactoryType,
                pluginContext.adaptiveFragmentFactoryClass.constructors.first(),
                1, 0,
                Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_COUNT,
            ).apply {
                putTypeArgument(0, classBoundBridgeType.defaultType)
                putValueArgument(
                    Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARING_FRAGMENT,
                    irGet(patchFun.dispatchReceiverParameter!!)
                )
                putValueArgument(
                    Indices.ADAPTIVE_FRAGMENT_FACTORY_ARGUMENT_DECLARATION_INDEX,
                    irConst(argument.fragmentIndex)
                )
            }
        )

}