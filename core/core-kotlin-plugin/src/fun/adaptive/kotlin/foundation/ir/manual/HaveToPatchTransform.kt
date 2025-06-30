/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.manual

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

class HaveToPatchTransform(
    override val pluginContext: FoundationPluginContext,
    val dirtyMask : IrProperty,
    val haveToPatchVariable: IrSimpleFunctionSymbol,
    val haveToPatchMask: IrSimpleFunctionSymbol,
    val propertyMap: Map<IrSimpleFunctionSymbol, Int>
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol != haveToPatchVariable) return super.visitCall(expression)

        val index = propertyMap[(expression.getValueArgument(0) as IrCall).symbol]
        check(index != null) { "cannot find state variable index for ${expression.dumpKotlinLike()}" }

        return irCall(
            haveToPatchMask,
            expression.dispatchReceiver,
            args = arrayOf(
                irGetValue(dirtyMask, receiver = expression.dispatchReceiver).deepCopyWithSymbols(),
                irConst(1 shl index)
            )
        )
    }

}
