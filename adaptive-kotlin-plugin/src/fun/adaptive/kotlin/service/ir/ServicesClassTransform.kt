/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.service.ir

import `fun`.adaptive.kotlin.common.debug
import `fun`.adaptive.kotlin.common.isSubclassOf
import `fun`.adaptive.kotlin.service.FqNames
import `fun`.adaptive.kotlin.service.ir.consumer.ConsumerClassTransform
import `fun`.adaptive.kotlin.service.ir.impl.ImplClassTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.hasAnnotation

class ServicesClassTransform(
    private val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement =

        when {

            declaration.isSubclassOf(pluginContext.serviceImplClass) -> {
                ImplClassTransform(pluginContext).visitClass(declaration)
            }

            declaration.hasAnnotation(FqNames.SERVICE_API) -> {
                debug { declaration.dumpKotlinLike() }
                ConsumerClassTransform(pluginContext).visitClass(declaration)
            }

            else -> super.visitClassNew(declaration)

        }

}
