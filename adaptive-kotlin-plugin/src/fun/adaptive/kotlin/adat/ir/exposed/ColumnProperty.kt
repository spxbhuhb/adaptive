/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.adat.ir.exposed

import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName

class ColumnProperty(
    val property: IrProperty,
    val type: IrType
) {
    override fun toString(): String {
        return "${property.name}: ${type.classFqName}"
    }
}