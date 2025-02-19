/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmClassBuilder
import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ArmEntryPointBuilder
import `fun`.adaptive.kotlin.foundation.ir.ir2arm.EntryPointTransform
import `fun`.adaptive.kotlin.foundation.ir.ir2arm.OriginalFunctionTransform
import `fun`.adaptive.kotlin.foundation.ir.manual.AdaptiveActualTransform
import `fun`.adaptive.kotlin.foundation.ir.reference.FunctionPropertyTransform
import `fun`.adaptive.utility.debug
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.FakeOverridesStrategy
import org.jetbrains.kotlin.ir.util.KotlinLikeDumpOptions
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

internal class FoundationGenerationExtension(
    val options: AdaptiveOptions
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        FoundationPluginContext(
            pluginContext,
            options,
        ).apply {

            // debug("DUMP BEFORE") { "\n\n" + moduleFragment.dump() }

            moduleFragment.accept(FunctionPropertyTransform(this), null)
            moduleFragment.accept(AdaptiveActualTransform(this), null)
            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            armClasses
                .map {
                    ArmClassBuilder(this, it).apply { buildIrClassWithoutGenBodies() }
                }
                .onEach {
                    if (! it.armClass.isRoot) { // root lambda have to stay the same
                        // this has to be before `buildGenFunctionBodies` or call transforms will fail
                        // because the function parameters are not fixed yet
                        it.buildOriginalFunctionBody()
                    }
                }
                .forEach {
                    it.buildGenFunctionBodies()
                }

            armEntryPoints
                .forEach { ArmEntryPointBuilder(this, it).entryPointBody() }

//            moduleFragment.dump().debug()
//            moduleFragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE)).debug()

            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dumpKotlinLike(KotlinLikeDumpOptions(printFakeOverridesStrategy = FakeOverridesStrategy.NONE)) }
            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
        }
    }

}

