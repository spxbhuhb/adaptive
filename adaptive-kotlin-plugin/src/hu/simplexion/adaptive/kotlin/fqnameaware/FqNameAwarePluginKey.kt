/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.fqnameaware

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin

object FqNameAwarePluginKey : GeneratedDeclarationKey() {
    val origin = IrDeclarationOrigin.GeneratedByPlugin(FqNameAwarePluginKey)
}
