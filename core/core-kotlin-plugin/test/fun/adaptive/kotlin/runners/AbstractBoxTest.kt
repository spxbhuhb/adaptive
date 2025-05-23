/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.runners

import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.test.FirParser
import org.jetbrains.kotlin.test.TargetBackend
import org.jetbrains.kotlin.test.backend.BlackBoxCodegenSuppressor
import org.jetbrains.kotlin.test.backend.handlers.IrTextDumpHandler
import org.jetbrains.kotlin.test.backend.handlers.IrTreeVerifierHandler
import org.jetbrains.kotlin.test.backend.handlers.JvmBoxRunner
import org.jetbrains.kotlin.test.backend.ir.JvmIrBackendFacade
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.builders.fir2IrStep
import org.jetbrains.kotlin.test.builders.irHandlersStep
import org.jetbrains.kotlin.test.builders.jvmArtifactsHandlersStep
import org.jetbrains.kotlin.test.directives.CodegenTestDirectives.DUMP_IR
import org.jetbrains.kotlin.test.directives.configureFirParser
import org.jetbrains.kotlin.test.model.DependencyKind
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.runners.RunnerWithTargetBackendForTestGeneratorMarker
import org.jetbrains.kotlin.test.services.RuntimeClasspathProvider
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

/*
 * Containers of different directives, which can be used in tests:
 * - ModuleStructureDirectives
 * - LanguageSettingsDirectives
 * - DiagnosticsDirectives
 * - CodegenTestDirectives
 *
 * All of them are located in `org.jetbrains.kotlin.test.directives` package
 */
open class AbstractBoxTest : BaseTestRunner(), RunnerWithTargetBackendForTestGeneratorMarker {
    override val targetBackend: TargetBackend
        get() = TargetBackend.JVM_IR

    override fun configure(builder: TestConfigurationBuilder) {
        with(builder) {
            globalDefaults {
                targetBackend = TargetBackend.JVM_IR
                targetPlatform = JvmPlatforms.defaultJvmPlatform
                dependencyKind = DependencyKind.Binary
            }

            val dumps = false

            useCustomRuntimeClasspathProviders(AbstractBoxTest::coreRuntimeClassPathProvider)

            configureFirParser(FirParser.Psi)

            if (dumps) {
                defaultDirectives {
                    + DUMP_IR
                }
            }
            commonFirWithPluginFrontendConfiguration(dumpFir = dumps)
            fir2IrStep()
            irHandlersStep {
                if (dumps) {
                    useHandlers(
                        ::IrTextDumpHandler,
                        ::IrTreeVerifierHandler,
                    )
                } else {
                    useHandlers(
                        ::IrTreeVerifierHandler
                    )
                }
            }
            facadeStep(::JvmIrBackendFacade)

            jvmArtifactsHandlersStep {
                useHandlers(::JvmBoxRunner)
            }

            useAfterAnalysisCheckers(::BlackBoxCodegenSuppressor)
        }
    }

    class coreRuntimeClassPathProvider(testServices: TestServices) : RuntimeClasspathProvider(testServices) {
        override fun runtimeClassPaths(module: TestModule): List<File> = runtimeClassPath()
    }
}