/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.foundation.ir.ir2arm

import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationPluginContext
import hu.simplexion.adaptive.kotlin.foundation.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.SpecialNames

/**
 * - creates an `ArmClass` for each original function (a function annotated with `@Adaptive`)
 * - replaces the body of each original function with an empty one
 */
class OriginalFunctionTransform(
    override val pluginContext: FoundationPluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    /**
     * Transforms a function annotated with `@Adaptive` into an Adaptive fragment class.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (declaration.isAnonymousFunction || declaration.name == SpecialNames.ANONYMOUS) return declaration

        if (! declaration.isAdaptive) {
            return declaration
        }

        val body = checkNotNull(declaration.body) { "missing function body\${declaration.dumpKotlinLike()}\n${declaration.dump()}" }

        // do not transform manual implementations
        if (body.statements.size == 1) {
            val statement = body.statements[0]
            if (statement is IrCall && statement.symbol == pluginContext.manualImplementation) {
                return declaration
            }
        }

        // this is the actual transform
        IrFunction2ArmClass(pluginContext, declaration, false).transform()

        return declaration
    }

}
