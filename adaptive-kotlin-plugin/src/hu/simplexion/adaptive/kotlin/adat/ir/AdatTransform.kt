/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty

class AdatTransform(
    private val pluginContext: AdatPluginContext
) : IrElementTransformerVoidWithContext() {

    override fun visitClassNew(declaration: IrClass): IrStatement {

//        if (! declaration.defaultType.isSubtypeOfClass(pluginContext.fqNameAwareClass)) return declaration
//        if (declaration.isInterface) return declaration

        return super.visitClassNew(declaration)
    }

    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
//        if (declaration.origin != FqNameAwarePluginKey.origin) return declaration
//
//        transformProperty(
//            pluginContext,
//            declaration,
//            backingField = false,
//        ) { irConst(declaration.parentAsClass.kotlinFqName.asString()) }

        return declaration
    }

}
