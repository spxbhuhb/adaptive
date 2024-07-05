/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.adatclass

import hu.simplexion.adaptive.kotlin.adat.AdatPluginKey
import hu.simplexion.adaptive.kotlin.adat.Names
import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import hu.simplexion.adaptive.kotlin.adat.ir.adatcompanion.AdatCompanionTransform
import hu.simplexion.adaptive.kotlin.adat.ir.metadata.MetadataVisitor
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.isFakeOverride
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class AdatClassTransform(
    private val pluginContext: AdatPluginContext,
    private val adatClass : IrClass
) : IrElementTransformerVoidWithContext() {

    val properties = MetadataVisitor(pluginContext, adatClass).also { adatClass.acceptVoid(it) }.properties

    override fun visitClassNew(declaration: IrClass): IrStatement {
        when {
            declaration.isSubclassOf(pluginContext.adatCompanion.owner) -> {
                AdatCompanionTransform(pluginContext, declaration, properties.map { it.metadata }).transform()
            }
        }

        return declaration
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {

        when (declaration.name) {
            Names.ADAT_COMPANION -> AdatCompanionPropertyTransform(pluginContext, adatClass).transform(declaration)
        }

        return declaration
    }

    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (declaration.isFakeOverride) return declaration

        when (declaration.name) {
            Names.GEN_GET_VALUE -> GenGetValueFunctionTransform(pluginContext, adatClass, properties, declaration).transform()
            Names.GEN_SET_VALUE -> GenSetValueFunctionTransform(pluginContext, adatClass, properties, declaration).transform()
            Names.EQUALS -> EqualsFunctionTransform(pluginContext, adatClass, declaration).transform()
            Names.HASHCODE -> HashCodeFunctionTransform(pluginContext, adatClass, declaration).transform()
            Names.TO_STRING -> ToStringFunctionTransform(pluginContext, adatClass, declaration).transform()
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
