/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.reference

import `fun`.adaptive.kotlin.common.AdaptiveFqNames.PLUGIN_REFERENCE
import `fun`.adaptive.kotlin.common.firstRegularArgument
import `fun`.adaptive.kotlin.common.firstRegularParameter
import `fun`.adaptive.kotlin.common.secondRegularArgument
import `fun`.adaptive.kotlin.foundation.ClassIds
import `fun`.adaptive.kotlin.foundation.Strings
import `fun`.adaptive.kotlin.foundation.ir.FoundationPluginContext
import `fun`.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImplWithShape
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.getAnnotationArgumentValue
import org.jetbrains.kotlin.ir.util.hasAnnotation

/**
 * Transforms function type properties which `@Adaptive` annotation to match
 * general adaptive function signature.
 *
 * ```kotlin
 * val a : @Adaptive () -> Unit = ::b
 *
 * // is transformed into
 *
 * val a : @Adaptive (AdaptiveFragment, Int) -> Unit = ::b
 * ```
 */
class FunctionPropertyTransform(
    override val pluginContext: FoundationPluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension {

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

    override fun visitCall(expression: IrCall): IrExpression {
        val owner = expression.symbol.owner
        val name = owner.name

        if (name.isSpecial || name.identifier != Strings.ADD) return super.visitCall(expression)

        if (owner.getAnnotationArgumentValue<String>(PLUGIN_REFERENCE, "key") != "addNonTransformed") return super.visitCall(expression)

        //if (pluginContext.addNonTransformed !in owner.overriddenSymbols) return super.visitCall(expression)

        return IrCallImpl(
            SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
            pluginContext.irBuiltIns.unitType,
            pluginContext.addTransformed,
            typeArgumentsCount = 0
        ).apply {
            arguments[0] = expression.dispatchReceiver
            arguments[1] = expression.firstRegularArgument
            arguments[2] = visitFunctionReference(expression.secondRegularArgument as IrFunctionReference)
        }
    }

    /**
     * Transform parameters of all non-adaptive functions.
     */
    override fun visitValueParameterNew(declaration: IrValueParameter): IrStatement {
        if (declaration.isAdaptive) {
            declaration.type = pluginContext.adaptiveFunctionType
        }
        return super.visitValueParameterNew(declaration)
    }

    /**
     * Handle direct property declarations and in-constructor property declarations.
     */
    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        val backingField = declaration.backingField ?: return declaration

        if ( ! backingField.type.hasAnnotation(ClassIds.ADAPTIVE)) {
            return super.visitPropertyNew(declaration)
        }

        val getter = declaration.getter
        check(getter != null) { "only properties with getter are supported as @Adaptive" }

        val newType = pluginContext.adaptiveFunctionType

        backingField.type = newType
        backingField.initializer !!.expression.type = newType

        getter.returnType = newType
        getter.body?.transform(GetterReturnTypeTransform(pluginContext), null)

        declaration.setter?.let { setter ->
            setter.firstRegularParameter.type = newType
        }

        return super.visitPropertyNew(declaration)
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
