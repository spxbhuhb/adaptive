/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.service

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.AdaptivePluginRegistrar
import `fun`.adaptive.kotlin.adat.ir.AdatGenerationExtension
import `fun`.adaptive.kotlin.debug.ir.DebugGenerationExtension
import `fun`.adaptive.kotlin.foundation.ir.FoundationGenerationExtension
import `fun`.adaptive.kotlin.reflect.ir.ReflectGenerationExtension
import `fun`.adaptive.kotlin.backend.ir.BackendGenerationExtension
import `fun`.adaptive.kotlin.common.debug
import `fun`.adaptive.kotlin.service.ir.ServicesGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices

class ExtensionRegistrarConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration
    ) {
        val options = AdaptiveOptions(
            pluginDebug = true,
            debugFilter = Regex(".*"),
            pluginLogDir = null, // File("testData/log"),
            dumpKotlinLike = false,
            dumpIR = false
        )

        FirExtensionRegistrarAdapter.registerExtension(AdaptivePluginRegistrar())

        IrGenerationExtension.registerExtension(ServicesGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdatGenerationExtension(options))
        IrGenerationExtension.registerExtension(FoundationGenerationExtension(options))
        IrGenerationExtension.registerExtension(BackendGenerationExtension(options))
        IrGenerationExtension.registerExtension(ReflectGenerationExtension(options))
        IrGenerationExtension.registerExtension(DebugGenerationExtension(options))

    }
}