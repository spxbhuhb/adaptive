/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir.exposed

import hu.simplexion.adaptive.kotlin.adat.ir.AdatPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.types.typeOrNull
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class ColumnVisitor(
    val pluginContext: AdatPluginContext,
    tableClass: IrClass
) : IrElementVisitorVoid {

    val columns = mutableSetOf<ColumnProperty>()

    init {
        tableClass.acceptVoid(this)
    }

    override fun visitElement(element: IrElement) {
        element.acceptChildrenVoid(this)
    }

    override fun visitProperty(declaration: IrProperty) {
        val type = declaration.backingField?.type ?: return
        if (type !is IrSimpleType) return

        val valueType = type.arguments.first().typeOrNull ?: return

        if (! type.isSubtypeOfClass(pluginContext.exposedColumn !!)) return

        columns += ColumnProperty(
            declaration,
            valueType
        )
    }

}