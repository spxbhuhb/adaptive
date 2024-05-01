/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.fqnameaware.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import hu.simplexion.adaptive.kotlin.fqnameaware.ClassIds
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

class FqNameAwarePluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val fqNameAwareClass = ClassIds.FQ_NAME_AWARE.classSymbol()

}