/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion.AdatCompanionTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class AdatClassTransform(
    private val pluginContext: AdatPluginContext,
    private val adatClass : IrClass
) : IrElementTransformerVoidWithContext() {

    var propertyIndex = 0

    override fun visitClassNew(declaration: IrClass): IrStatement {
        when {
            declaration.isSubclassOf(pluginContext.adatCompanion.owner) -> {
                declaration.transformChildrenVoid(AdatCompanionTransform(pluginContext, declaration))
            }
        }

        return declaration
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {

        when (declaration.name) {
            Names.ADAT_VALUES -> declaration.transform(AdatValuesPropertyTransform(pluginContext, adatClass), null)
            Names.ADAT_COMPANION -> declaration.transform(AdatCompanionPropertyTransform(pluginContext, adatClass), null)
            else -> declaration.transform(PropertyTransform(pluginContext, adatClass, propertyIndex++), null)
        }

        return declaration
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (declaration.isFakeOverride) return declaration

        when (declaration.name) {
            Names.EQUALS -> declaration.transform(EqualsFunctionTransform(pluginContext, adatClass, declaration), null)
            Names.HASHCODE -> declaration.transform(HashCodeFunctionTransform(pluginContext, adatClass, declaration), null)
        }

        return declaration
    }

    override fun visitConstructor(declaration: IrConstructor): IrStatement {
        when {
            declaration.origin == AdatPluginKey.origin && declaration.valueParameters.isEmpty() -> {
                declaration.transform(EmptyConstructorTransform(pluginContext, adatClass, declaration), null)
            }
            declaration.origin == AdatPluginKey.origin && declaration.valueParameters.size == 1 -> {
                declaration.transform(ValuesConstructorTransform(pluginContext, adatClass, declaration), null)
            }
            else -> {
                declaration.transform(OriginalConstructorTransform(pluginContext, adatClass, declaration), null)
            }
        }
        return declaration
    }

}
