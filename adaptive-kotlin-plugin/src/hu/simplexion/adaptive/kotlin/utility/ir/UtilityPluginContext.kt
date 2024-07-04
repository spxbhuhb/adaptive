/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.utility.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

class UtilityPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options)