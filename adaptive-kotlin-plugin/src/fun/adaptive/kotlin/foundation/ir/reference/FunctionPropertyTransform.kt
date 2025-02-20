/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.reference

import `fun`.adaptive.kotlin.foundation.ClassIds
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImplWithShape
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.primaryConstructor

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

    /**
     * Do not transform parameters of adaptive functions as we need those later,
     * when we transform the function itself.
     *
     * Also, skip adaptive entry functions as they are a special case.
     */
    override fun visitSimpleFunction(declaration: IrSimpleFunction): IrStatement {
        if (declaration.hasAnnotation(ClassIds.ADAPTIVE)) return declaration
        if (declaration.hasAnnotation(ClassIds.ADAPTIVE_ENTRY)) return declaration
        return super.visitSimpleFunction(declaration)
    }

    /**
     * Transform parameters of all non-adaptive functions.
     */
    override fun visitValueParameterNew(declaration: IrValueParameter): IrStatement {
        if (! declaration.hasAnnotation(ClassIds.ADAPTIVE)) return declaration
        declaration.type = pluginContext.adaptiveFunctionType
        return super.visitValueParameterNew(declaration)
    }

    /**
     * Handle direct property declarations and in-constructor property declarations.
     */
    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        val backingField = declaration.backingField ?: return declaration
        val initializer = backingField.initializer ?: return declaration
        val expression = initializer.expression as? IrGetValue

        if (expression?.origin == IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER) {
            if (! isFromAdaptiveParameter(declaration)) return declaration
        } else {
            if (! declaration.hasAnnotation(ClassIds.ADAPTIVE)) return declaration
        }

        val getter = declaration.getter
        check(getter != null) { "only properties with getter are supported as @Adaptive" }

        val newType = pluginContext.adaptiveFunctionType

        backingField.type = newType
        backingField.initializer !!.expression.type = newType

        getter.returnType = newType
        getter.body?.transform(GetterReturnTypeTransform(pluginContext), null)

        declaration.setter?.let { setter ->
            setter.valueParameters[0].type = newType
        }

        return super.visitPropertyNew(declaration)
    }

    private fun isFromAdaptiveParameter(property: IrProperty): Boolean {
        val parent = property.parent
        val name = property.name

        val isAdaptive = when (parent) {
            is IrSimpleFunction -> parent.valueParameters
            is IrClass -> parent.primaryConstructor?.valueParameters
            else -> null
        }
            ?.first { it.symbol.owner.name == name }?.hasAnnotation(ClassIds.ADAPTIVE)

        return isAdaptive == true
    }

    override fun visitFunctionReference(expression: IrFunctionReference): IrExpression {
        val function = expression.symbol.owner

        if (! function.hasAnnotation(ClassIds.ADAPTIVE)) {
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
