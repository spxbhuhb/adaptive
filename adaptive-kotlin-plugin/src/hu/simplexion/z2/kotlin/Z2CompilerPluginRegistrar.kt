/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptiveGenerationExtension
import hu.simplexion.z2.kotlin.services.ir.ServicesGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

/**
 * Registers the extensions into the compiler.
 */
@OptIn(ExperimentalCompilerApi::class)
class Z2CompilerPluginRegistrar : CompilerPluginRegistrar() {

    override val supportsK2 = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {

        val options = Z2Options(
            resourceOutputDir = configuration.get(Z2CommandLineProcessor.CONFIG_KEY_RESOURCE_DIR),
            pluginDebug = configuration.get(Z2CommandLineProcessor.CONFIG_KEY_PLUGIN_DEBUG) ?: false,
            pluginLogDir = configuration.get(Z2CommandLineProcessor.CONFIG_KEY_PLUGIN_LOG_DIR),
            dumpKotlinLike = configuration.get(Z2CommandLineProcessor.CONFIG_KEY_PLUGIN_DEBUG) ?: false
        )

        FirExtensionRegistrarAdapter.registerExtension(Z2PluginRegistrar())

        // if you add something here, add also to ExtensionRegistrarConfigurator for tests
        IrGenerationExtension.registerExtension(ServicesGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdaptiveGenerationExtension(options))
    }

}
