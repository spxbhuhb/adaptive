package hu.simplexion.z2.kotlin.services

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.Z2PluginRegistrar
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptiveGenerationExtension
import hu.simplexion.z2.kotlin.services.ir.ServicesGenerationExtension
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
        val options = Z2Options(
            resourceOutputDir = File("testData/generated"),
            pluginDebug = true,
            pluginLogDir = null, //File("testData/log"),
            dumpKotlinLike = true
        )

        FirExtensionRegistrarAdapter.registerExtension(Z2PluginRegistrar())

        IrGenerationExtension.registerExtension(ServicesGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdaptiveGenerationExtension(options))
    }
}