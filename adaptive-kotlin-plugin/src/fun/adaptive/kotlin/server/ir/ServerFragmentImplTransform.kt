/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.server.ir

import `fun`.adaptive.kotlin.common.transformProperty
import `fun`.adaptive.kotlin.server.Names
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.isInterface
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.parentAsClass

class ServerFragmentImplTransform(
    private val pluginContext: ServerPluginContext,
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        if (! declaration.defaultType.isSubtypeOfClass(pluginContext.serverFragmentImplClass)) return declaration
        if (declaration.isInterface) return declaration

        return super.visitClassNew(declaration)
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        when (declaration.name) {

            Names.FRAGMENT_PROPERTY -> {
                transformProperty(
                    pluginContext,
                    declaration,
                    backingField = true,
                ) { irNull() }
            }

            Names.LOGGER_PROPERTY -> {
                transformProperty(
                    pluginContext,
                    declaration,
                    backingField = true,
                ) { irNull() }
            }
        }

        return declaration
    }

}
