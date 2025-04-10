/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.debug.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.FakeOverridesStrategy
import org.jetbrains.kotlin.ir.util.KotlinLikeDumpOptions
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

internal class DebugGenerationExtension(
    val options: AdaptiveOptions,
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        //println(moduleFragment.dump())

        with(DebugPluginContext(pluginContext, options)) {
            debug {
                pluginContext.platform
            }
            moduleFragment.files.forEach { file ->
                file.declarations.forEach {
                    if (it.symbol.toString().matches(options.debugFilter)) {
                        if (options.dumpKotlinLike) {
                            debug {
                                it.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE))
                            }
                        }
                        if (options.dumpIR) {
                            debug {
                                it.dump()
                            }
                        }
                    }
                }
            }
        }
    }
}