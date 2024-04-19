package hu.simplexion.z2.kotlin.runners

import hu.simplexion.z2.kotlin.services.ExtensionRegistrarConfigurator
import hu.simplexion.z2.kotlin.services.PluginAnnotationsProvider
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives
import org.jetbrains.kotlin.test.initIdeaConfiguration
import org.jetbrains.kotlin.test.runners.AbstractKotlinCompilerTest
import org.jetbrains.kotlin.test.runners.baseFirDiagnosticTestConfiguration
import org.jetbrains.kotlin.test.services.EnvironmentBasedStandardLibrariesPathProvider
import org.jetbrains.kotlin.test.services.KotlinStandardLibrariesPathProvider
import org.junit.jupiter.api.BeforeAll

abstract class BaseTestRunner : AbstractKotlinCompilerTest() {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            initIdeaConfiguration()
        }
    }

    override fun createKotlinStandardLibrariesPathProvider(): KotlinStandardLibrariesPathProvider {
        return EnvironmentBasedStandardLibrariesPathProvider
    }
}

fun TestConfigurationBuilder.commonFirWithPluginFrontendConfiguration(dumpFir : Boolean = true) {
    baseFirDiagnosticTestConfiguration()

    defaultDirectives {
        + FirDiagnosticsDirectives.ENABLE_PLUGIN_PHASES
        if (dumpFir) + FirDiagnosticsDirectives.FIR_DUMP
    }

    useConfigurators(
        ::PluginAnnotationsProvider,
        ::ExtensionRegistrarConfigurator
    )
}