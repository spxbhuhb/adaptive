/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class AdatGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        AdatPluginContext(pluginContext, options).apply {

            // debug("DUMP BEFORE") { "\n\n" + moduleFragment.dump() }

            moduleFragment.transformChildrenVoid(AdatModuleTransform(this))
            moduleFragment.transformChildrenVoid(AdatCallTransform(this))

            // I don't like the syntax of this function, confusing and too general
            // I'll comment this out until I figure out a better solution
            // moduleFragment.transformChildrenVoid(UpdateVisitor(this))

            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE)) }
            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
        }
    }


}