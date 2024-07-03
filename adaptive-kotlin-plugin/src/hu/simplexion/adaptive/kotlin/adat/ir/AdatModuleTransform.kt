/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir

import hu.simplexion.adaptive.kotlin.adat.ClassIds
import hu.simplexion.adaptive.kotlin.adat.ir.adatclass.AdatClassTransform
import hu.simplexion.adaptive.kotlin.adat.ir.exposed.ExposedAdatTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class AdatModuleTransform(
    private val pluginContext: AdatPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {
        when {
            declaration.isSubclassOf(pluginContext.adatClass.owner) -> {
                declaration.transformChildrenVoid(AdatClassTransform(pluginContext, declaration))
            }
            declaration.hasAnnotation(ClassIds.EXPOSED_ADAT) -> {
                if (pluginContext.exposedColumn != null) {
                    ExposedAdatTransform(pluginContext, declaration).transform()
                }
            }
        }

        return declaration
    }

}
