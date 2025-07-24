/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.manual

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.foundation.FqNames
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.kotlinFqName

class StateSizeTransform(
    override val pluginContext: FoundationPluginContext,
    val stateSize : Int
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall): IrExpression {

        for (parameter in expression.symbol.owner.parameters) {
            val argument = expression.arguments[parameter] ?: continue
            if (argument !is IrCall) continue
            if (argument.symbol.owner.kotlinFqName != FqNames.STATE_SIZE) continue

            expression.arguments[parameter] = irConst(stateSize)
        }

        return expression
    }

}
