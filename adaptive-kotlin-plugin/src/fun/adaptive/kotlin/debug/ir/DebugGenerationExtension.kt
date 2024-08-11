/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.debug.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.*

internal class DebugGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        with (DebugPluginContext(pluginContext, options)) {
            if (options.dumpKotlinLike) {
                debug {
                    pluginContext.platform
                }
                debug {
                    moduleFragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE))
                }
            }
            if (options.dumpIR) {
                debug {
                    pluginContext.platform
                }
                debug {
                    moduleFragment.dump()
                }
            }
        }
    }

}

