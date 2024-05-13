/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir.ir2arm

import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.base.ir.util.AdaptiveAnnotationBasedExtension
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.SpecialNames

/**
 * - creates an `ArmClass` for each original function (a function annotated with `@Adaptive`)
 * - replaces the body of each original function with an empty one
 */
class OriginalFunctionTransform(
    override val pluginContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension {

    val irBuiltIns = pluginContext.irContext.irBuiltIns

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

        // replace the body of the original function with an empty one
        declaration.body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET)

        return declaration
    }

}
