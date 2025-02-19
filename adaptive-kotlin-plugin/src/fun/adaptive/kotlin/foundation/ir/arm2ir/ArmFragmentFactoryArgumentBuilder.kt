/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.foundation.ir.arm2ir

import `fun`.adaptive.kotlin.foundation.ir.arm.ArmClosure
import `fun`.adaptive.kotlin.foundation.ir.arm.ArmFragmentFactoryArgument
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors

class ArmFragmentFactoryArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmFragmentFactoryArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    /**
     * When the declaration is a hard-coded lambda, we do not have to patch it as
     * it cannot change.
     */
    override fun patchDirtyMask(): IrExpression {
        val expression = argument.irExpression
        if (expression is IrFunctionExpression && expression.function.origin == IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA) {
            return irConst(0)
        } else {
            return super.patchDirtyMask()
        }
    }

    override fun patchVariableValue(patchFun: IrSimpleFunction): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            argument.argumentIndex,
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                pluginContext.boundFragmentFactoryType,
                pluginContext.boundFragmentFactoryClass.constructors.first(),
                typeArgumentsCount = 0,
                constructorTypeArgumentsCount = 0
            ).apply {
                putValueArgument(
                    0,
                    irGet(patchFun.dispatchReceiverParameter !!)
                )
                putValueArgument(
                    1,
                    irConst(argument.fragmentIndex)
                )
                putValueArgument(
                    2,
                    if (argument.irExpression is IrFunctionExpression) {
                        irNull()
                    } else {
                        // covers function reference based calls such as direct references and property getters
                        argument.irExpression.transformCreateStateAccess(closure, patchFun) { irGet(fragment) }
                    }
                )
            }
        )

}