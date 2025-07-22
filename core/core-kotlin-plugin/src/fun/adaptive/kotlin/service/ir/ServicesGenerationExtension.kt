/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.service.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.service.ir.consumer.GetConsumerTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.FakeOverridesStrategy
import org.jetbrains.kotlin.ir.util.KotlinLikeDumpOptions
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class ServicesGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        ServicesPluginContext(pluginContext, options).apply {
            moduleFragment.transformChildrenVoid(ServicesClassTransform(this))
            moduleFragment.transformChildrenVoid(GetConsumerTransform(this))
            debug("DUMP AFTER") { "\n\n" + moduleFragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE)) }
            //debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
        }
    }

}