/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir

import `fun`.adaptive.kotlin.adat.ClassIds
import `fun`.adaptive.kotlin.adat.ir.adatclass.AdatClassTransform
import `fun`.adaptive.kotlin.adat.ir.adatcompanion.AdatCompanionTransform
import `fun`.adaptive.kotlin.adat.ir.exposed.ExposedAdatTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class AdatModuleTransform(
    private val pluginContext: AdatPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {
        when {

            declaration.isSubclassOf(pluginContext.adatClass.owner) && declaration.modality != Modality.ABSTRACT && declaration.modality != Modality.SEALED -> {
                val transform = AdatClassTransform(pluginContext, declaration)

                declaration.acceptChildrenVoid(transform)

                val companion = declaration.companionObject()
                requireNotNull(companion) { "ADAT class ${declaration.classId} must have a companion object" }

                AdatCompanionTransform(
                    pluginContext,
                    companion,
                    transform.metadata.zip()
                ).transform()
            }

            declaration.hasAnnotation(ClassIds.EXPOSED_ADAT_TABLE) -> {
                if (pluginContext.exposedColumn != null) {
                    declaration.transformChildrenVoid(ExposedAdatTransform(pluginContext, declaration))
                }
            }

        }

        return declaration
    }

}
