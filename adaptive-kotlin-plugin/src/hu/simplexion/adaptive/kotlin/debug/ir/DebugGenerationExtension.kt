/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.debug.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ArmClassBuilder
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ArmEntryPointBuilder
import hu.simplexion.adaptive.kotlin.base.ir.ir2arm.EntryPointTransform
import hu.simplexion.adaptive.kotlin.base.ir.ir2arm.OriginalFunctionTransform
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
                    moduleFragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE))
                }
            }
        }
    }

}

