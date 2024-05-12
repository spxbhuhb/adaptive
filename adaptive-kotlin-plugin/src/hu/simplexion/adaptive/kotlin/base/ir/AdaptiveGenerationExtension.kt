/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.base.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ArmClassBuilder
import hu.simplexion.adaptive.kotlin.base.ir.arm2ir.ArmEntryPointBuilder
import hu.simplexion.adaptive.kotlin.base.ir.ir2arm.EntryPointTransform
import hu.simplexion.adaptive.kotlin.base.ir.ir2arm.OriginalFunctionTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.*

internal class AdaptiveGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        AdaptivePluginContext(
            pluginContext,
            options,
        ).apply {

            // debug("DUMP BEFORE") { "\n\n" + moduleFragment.dump() }

            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            armClasses
                .map {
                    ArmClassBuilder(this, it).apply { buildIrClassWithoutGenBodies() }
                }
                .forEach {
                    it.buildGenFunctionBodies()
                    it.armClass.originalFunction.file.addChild(it.irClass)
                }

            moduleFragment.accept(ClassFunctionTransform(this), null)

            armEntryPoints
                .forEach { ArmEntryPointBuilder(this, it).entryPointBody() }

            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
        }
    }

}

