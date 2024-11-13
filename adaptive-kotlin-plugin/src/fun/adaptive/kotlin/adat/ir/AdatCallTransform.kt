/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrGetObjectValueImpl
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

class AdatCallTransform(
    private val pluginContext: AdatPluginContext,
) : IrElementTransformerVoidWithContext() {

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol != pluginContext.adatCompanionOfFun) {
            return super.visitCall(expression)
        }

        val companion = expression.getTypeArgument(0)?.classOrNull?.owner?.companionObject()

        checkNotNull(companion) { "cannot find companion object in ${expression.dumpKotlinLike()}" }

        return IrGetObjectValueImpl(
            expression.startOffset,
            expression.endOffset,
            companion.defaultType,
            companion.symbol
        )
    }

}
