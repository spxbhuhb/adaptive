/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.services.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.services.ir.consumer.GetConsumerTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class ServicesGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
//        println("==== BEFORE ====")
//        println(moduleFragment.dump())

        ServicesPluginContext(pluginContext, options).apply {
            moduleFragment.transformChildrenVoid(ServicesClassTransform(this))
            moduleFragment.transformChildrenVoid(GetConsumerTransform(this))
        }

//        println("==== AFTER ====")
//        println(moduleFragment.dump())
    }

}