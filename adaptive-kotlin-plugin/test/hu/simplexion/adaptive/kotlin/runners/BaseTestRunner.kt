/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.runners

import hu.simplexion.adaptive.kotlin.service.ExtensionRegistrarConfigurator
import hu.simplexion.adaptive.kotlin.service.PluginAnnotationsProvider
import org.jetbrains.kotlin.test.backend.ir.JvmIrBackendFacade
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives
import org.jetbrains.kotlin.test.directives.JvmEnvironmentConfigurationDirectives
import org.jetbrains.kotlin.test.frontend.fir.Fir2IrJvmResultsConverter
import org.jetbrains.kotlin.test.frontend.fir.FirFrontendFacade
import org.jetbrains.kotlin.test.initIdeaConfiguration
import org.jetbrains.kotlin.test.model.FrontendKinds
import org.jetbrains.kotlin.test.runners.AbstractKotlinCompilerTest
import org.jetbrains.kotlin.test.runners.baseFirDiagnosticTestConfiguration
import org.jetbrains.kotlin.test.runners.codegen.commonConfigurationForTest
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

    commonConfigurationForTest(
        targetFrontend = FrontendKinds.FIR,
        frontendFacade = ::FirFrontendFacade,
        frontendToBackendConverter = ::Fir2IrJvmResultsConverter,
        backendFacade = ::JvmIrBackendFacade,
        commonServicesConfiguration = {},
    )

    defaultDirectives {
        + FirDiagnosticsDirectives.ENABLE_PLUGIN_PHASES
        if (dumpFir) + FirDiagnosticsDirectives.FIR_DUMP
        + JvmEnvironmentConfigurationDirectives.FULL_JDK
    }

    useConfigurators(
        ::PluginAnnotationsProvider,
        ::ExtensionRegistrarConfigurator
    )
}