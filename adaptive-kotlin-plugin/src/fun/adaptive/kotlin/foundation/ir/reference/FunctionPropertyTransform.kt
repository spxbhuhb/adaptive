/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.reference

import `fun`.adaptive.kotlin.foundation.ClassIds
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionReference
import org.jetbrains.kotlin.ir.expressions.IrGetField
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImplWithShape
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isFunction

/**
 * Transforms function type properties which `@Adaptive` annotation to match
 * general adaptive function signature.
 *
 * ```kotlin
 * @Adaptive
 * val a : () -> Unit = ::b
 *
 * // is transformed into
 *
 * @Adaptive
 * val a : (AdaptiveFragment, Int) -> Unit = ::b
 * ```
 */
class FunctionPropertyTransform(
    private val pluginContext: FoundationPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (! declaration.hasAnnotation(ClassIds.ADAPTIVE)) return super.visitPropertyNew(declaration)

        val backingField = declaration.backingField
        val getter = declaration.getter

        check(backingField != null) { "only properties with backing field are supported as @Adaptive" }
        check(getter != null) { "only properties with getter are supported as @Adaptive" }

        val type = backingField.type
        check(type.isFunction()) { "only function properties are supported as @Adaptive" }

        val newType = pluginContext.adaptiveFunctionType

        backingField.type = newType
        backingField.initializer!!.expression.type = newType

        getter.returnType = newType
        getter.body?.transform(GetterReturnTypeTransform(pluginContext), null)

        return super.visitPropertyNew(declaration)
    }

    override fun visitFunctionReference(expression: IrFunctionReference): IrExpression {
        val function = expression.symbol.owner

        if (!function.hasAnnotation(ClassIds.ADAPTIVE)) {
            return super.visitFunctionReference(expression)
        }

        return IrFunctionReferenceImplWithShape(
            expression.startOffset,
            expression.endOffset,
            pluginContext.kFunctionAdaptiveReferenceType,
            expression.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 2,
            contextParameterCount = 0,
            hasDispatchReceiver = false,
            hasExtensionReceiver = false
        )
    }

    class GetterReturnTypeTransform(
        private val pluginContext: FoundationPluginContext
    ) : IrElementTransformerVoidWithContext() {

        override fun visitGetField(expression: IrGetField): IrExpression {
            expression.type = pluginContext.adaptiveFunctionType
            return expression
        }

    }

}
