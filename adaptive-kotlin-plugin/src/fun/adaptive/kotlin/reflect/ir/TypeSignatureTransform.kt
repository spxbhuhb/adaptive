/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.reflect.ir

import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import `fun`.adaptive.kotlin.wireformat.Signature
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass

class TypeSignatureTransform(
    override val pluginContext: ReflectPluginContext,
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    override fun visitCall(expression: IrCall): IrExpression {
        if (expression.symbol !in pluginContext.typeSignatureSymbol) {
            return super.visitCall(expression)
        }

        val receiver = expression.extensionReceiver
        var type : IrType

        if (receiver != null) {
            type = receiver.type
        } else {
            type = checkNotNull(expression.getTypeArgument(0))
        }

        type.classOrNull?.let {
            if (it.owner.isCompanion) type = it.owner.parentAsClass.defaultType
        }

        val signature = Signature.typeSignature(type, pluginContext.adatClass)

        return irConst(signature)
    }
}
