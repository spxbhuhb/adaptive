/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.backend.ir

import `fun`.adaptive.kotlin.common.transformProperty
import `fun`.adaptive.kotlin.backend.Names
import `fun`.adaptive.log.devInfo
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.util.*

class BackendFragmentImplTransform(
    private val pluginContext: BackendPluginContext,
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

        if (! declaration.defaultType.isSubtypeOfClass(pluginContext.workerImplClass)) return declaration

        return super.visitClassNew(declaration)
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        when (declaration.name) {
            Names.LOGGER_PROPERTY -> {
                transformProperty(
                    pluginContext,
                    declaration,
                    backingField = true,
                ) {
                    irCall(
                        this@BackendFragmentImplTransform.pluginContext.getLogger,
                        null,
                        irConst(declaration.parentClassOrNull?.name?.identifier ?: "Unknown")
                    )
                }
            }
        }

        return declaration
    }

}
