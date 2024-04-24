/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.services.ir.consumer.ConsumerClassTransform
import hu.simplexion.z2.kotlin.services.ir.impl.ImplClassTransform
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.defaultType

class ServicesClassTransform(
    private val pluginContext: ServicesPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        // create consumer class if subtype of `Service`

        if (declaration.defaultType.isSubtypeOfClass(pluginContext.serviceClass)) {
            ConsumerClassTransform(pluginContext, declaration).build()
            return declaration
        }

        // transform implementation if subtype of `ServiceImpl`

        if (declaration.defaultType.isSubtypeOfClass(pluginContext.serviceImplClass)) {
            return declaration.accept(ImplClassTransform(pluginContext), null) as IrStatement
        }

        return declaration
    }

}
