/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.fqnameaware.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class FqNameAwareGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        FqNameAwarePluginContext(pluginContext, options).apply {
            moduleFragment.transformChildrenVoid(FqNameAwareTransform(this))
        }
    }

}