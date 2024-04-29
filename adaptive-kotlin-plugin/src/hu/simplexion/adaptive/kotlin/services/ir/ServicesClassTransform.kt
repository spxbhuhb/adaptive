/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.services.ir

import hu.simplexion.adaptive.kotlin.services.ir.consumer.ConsumerClassTransform
import hu.simplexion.adaptive.kotlin.services.ir.impl.ImplClassTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.defaultType

class ServicesClassTransform(
    private val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        // transform implementation if subtype of `ServiceImpl`, must be before the `Service` transform

        if (declaration.defaultType.isSubtypeOfClass(pluginContext.serviceImplClass)) {
            return declaration.accept(ImplClassTransform(pluginContext), null) as IrStatement
        }

        // create consumer class if subtype of `Service`

        if (declaration.defaultType.isSubtypeOfClass(pluginContext.serviceClass)) {
            ConsumerClassTransform(pluginContext, declaration).build()
            return declaration
        }

        return declaration
    }

}
