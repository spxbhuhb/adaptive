/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir

import hu.simplexion.adaptive.kotlin.base.BasePluginKey
import hu.simplexion.adaptive.kotlin.base.Indices
import hu.simplexion.adaptive.kotlin.base.Strings
import hu.simplexion.adaptive.kotlin.base.ir.AdaptivePluginContext
import hu.simplexion.adaptive.kotlin.base.ir.util.AdaptiveAnnotationBasedExtension
import hu.simplexion.adaptive.kotlin.base.ir.util.adaptiveClassFqName
import hu.simplexion.adaptive.kotlin.common.AbstractIrBuilder
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.name.parentOrNull

/**
 * Generates the body of class functions:
 *
 * ```kotlin
 * fun adaptiveT0() : AdaptiveT0 {
 * }
 * ```
 */
class ClassFunctionTransform(
    override val pluginContext: AdaptivePluginContext
) : IrElementTransformerVoidWithContext(), AdaptiveAnnotationBasedExtension, AbstractIrBuilder {

    /**
     * Transforms a function annotated with `@Adaptive` into an Adaptive fragment class.
     */
    override fun visitFunctionNew(declaration: IrFunction): IrFunction {
        if (declaration.origin != BasePluginKey.origin) return declaration

        val name = declaration.name // set by the FIR plugin
        check(name.identifier.startsWith(Strings.CLASS_FUNCTION_PREFIX)) { "invalid class function name: $name" }

        val classSymbol = pluginContext.getSymbol(declaration.adaptiveClassFqName())
        checkNotNull(classSymbol) { "missing class or class function: ${declaration.adaptiveClassFqName()}" }

        val constructor = classSymbol.constructors.first()
        val valueParameters = declaration.valueParameters

        declaration.body = declaration.irReturnBody {
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                pluginContext.adaptiveFragmentClass.defaultType,
                constructor,
                0, 0, 3
            ).also {
                it.putValueArgument(0, irGet(valueParameters[0]))
                it.putValueArgument(1, irGet(valueParameters[1]))
                it.putValueArgument(2, irGet(valueParameters[2]))
            }
        }

        return declaration
    }

}
