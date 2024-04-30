/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.server.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.isInterface

class ServerFragmentImplClassTransform(
    private val pluginContext: ServerPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        if (! declaration.defaultType.isSubtypeOfClass(pluginContext.serverFragmentImplClass)) return declaration
        if (declaration.isInterface) return declaration

        ServerAdapterPropertyTransform(pluginContext, declaration).transform()

        return declaration
    }

}
