/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.service

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.AdaptivePluginRegistrar
import hu.simplexion.adaptive.kotlin.adat.ir.AdatGenerationExtension
import hu.simplexion.adaptive.kotlin.foundation.ir.AdaptiveGenerationExtension
import hu.simplexion.adaptive.kotlin.debug.ir.DebugGenerationExtension
import hu.simplexion.adaptive.kotlin.server.ir.ServerGenerationExtension
import hu.simplexion.adaptive.kotlin.service.ir.ServicesGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

class ExtensionRegistrarConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration
    ) {
        val options = AdaptiveOptions(
            resourceOutputDir = File("testData/generated"),
            pluginDebug = true,
            pluginLogDir = null, //File("testData/log"),
            dumpKotlinLike = true
        )

        FirExtensionRegistrarAdapter.registerExtension(AdaptivePluginRegistrar())

        IrGenerationExtension.registerExtension(ServicesGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdaptiveGenerationExtension(options))
        IrGenerationExtension.registerExtension(ServerGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdatGenerationExtension(options))
        IrGenerationExtension.registerExtension(DebugGenerationExtension(options))

    }
}