/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.service

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.AdaptivePluginRegistrar
import hu.simplexion.adaptive.kotlin.adat.ir.AdatGenerationExtension
import hu.simplexion.adaptive.kotlin.debug.ir.DebugGenerationExtension
import hu.simplexion.adaptive.kotlin.foundation.ir.FoundationGenerationExtension
import hu.simplexion.adaptive.kotlin.reflect.ir.ReflectGenerationExtension
import hu.simplexion.adaptive.kotlin.server.ir.ServerGenerationExtension
import hu.simplexion.adaptive.kotlin.service.ir.ServicesGenerationExtension
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
            pluginLogDir = null, // File("testData/log"),
            dumpKotlinLike = false,
            dumpIR = false
        )

        FirExtensionRegistrarAdapter.registerExtension(AdaptivePluginRegistrar())

        IrGenerationExtension.registerExtension(ServicesGenerationExtension(options))
        IrGenerationExtension.registerExtension(FoundationGenerationExtension(options))
        IrGenerationExtension.registerExtension(ServerGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdatGenerationExtension(options))
        IrGenerationExtension.registerExtension(ReflectGenerationExtension(options))
        IrGenerationExtension.registerExtension(DebugGenerationExtension(options))

    }
}