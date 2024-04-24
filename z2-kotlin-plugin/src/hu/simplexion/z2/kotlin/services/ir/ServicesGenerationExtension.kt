/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.services.ir.consumer.GetConsumerTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class ServicesGenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        ServicesPluginContext(pluginContext, options).apply {
            moduleFragment.transformChildrenVoid(ServicesClassTransform(this))
            moduleFragment.transformChildrenVoid(GetConsumerTransform(this))
        }
    }

}