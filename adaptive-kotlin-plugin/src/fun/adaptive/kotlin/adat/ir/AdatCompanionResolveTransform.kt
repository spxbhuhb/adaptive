/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir

import `fun`.adaptive.kotlin.adat.FqNames
import `fun`.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.backend.js.utils.typeArguments
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrGetObjectValueImpl
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.hasAnnotation
import kotlin.math.exp

class AdatCompanionResolveTransform(
    override val pluginContext: AdatPluginContext,
) : IrElementTransformerVoidWithContext(), AbstractIrBuilder {

    // @AdatCompanionResolve
    // fun <A : AdatClass> someFun(
    //     companion : AdatCompanion<A>? = null
    // )
    //
    // someFun<TestData>()

    override fun visitCall(expression: IrCall): IrExpression {
        val func = expression.symbol.owner
        if (! func.hasAnnotation(FqNames.ADAT_COMPANION_RESOLVE)) {
            return super.visitCall(expression)
        }

        val typeParameters = func.typeParameters

        if (typeParameters.isEmpty()) {
            return super.visitCall(expression)
        }

        val valueParameters = func.valueParameters.filter { it.type.isSubtypeOfClass(pluginContext.adatCompanion) }

        if (valueParameters.isEmpty()) {
            return super.visitCall(expression)
        }

        val typeArguments = expression.typeArguments

        for (parameter in valueParameters) {
            val value = expression.getValueArgument(parameter.index)
            if (value != null) continue // parameter passed directly

            val pParamSymbol = ((parameter.type as IrSimpleType).arguments.first() as IrSimpleType).classifier
            val tParam = typeParameters.firstOrNull { it.symbol == pParamSymbol }

            checkNotNull(tParam) { "cannot find type parameter for ${parameter.name} in ${expression.dumpKotlinLike()}" }

            val actualType = typeArguments[tParam.index]

            val companion = actualType?.classOrNull?.owner?.companionObject()

            checkNotNull(companion) { "cannot find companion object in ${expression.dumpKotlinLike()}" }

            expression.putValueArgument(
                parameter.index,
                IrGetObjectValueImpl(
                    expression.startOffset,
                    expression.endOffset,
                    companion.defaultType,
                    companion.symbol
                )
            )
        }

        return super.visitCall(expression)
    }
}
