/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.backend.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.common.AbstractPluginContext
import `fun`.adaptive.kotlin.backend.ClassIds
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType

class BackendPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val backendFragmentImplClass = ClassIds.BACKEND_FRAGMENT_IMPL.classSymbol()
    val backendFragmentType = ClassIds.BACKEND_FRAGMENT.classSymbol().defaultType

}