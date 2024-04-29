package hu.simplexion.z2.kotlin.services

import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices
import org.jetbrains.kotlin.test.services.assertions
import java.io.File
import java.io.FilenameFilter

class PluginAnnotationsProvider(testServices: TestServices) : EnvironmentConfigurator(testServices) {

    companion object {
        private const val ANNOTATIONS_JAR_DIR = "../z2-core/build/libs/"

        private val ANNOTATIONS_JAR_FILTER = FilenameFilter { _, name ->
            name.startsWith("z2-core-") && name.endsWith("-all.jar") && "sources" !in name
        }
    }

    override fun configureCompilerConfiguration(configuration: CompilerConfiguration, module: TestModule) {
        val libDir = File(ANNOTATIONS_JAR_DIR)
        testServices.assertions.assertTrue(libDir.exists() && libDir.isDirectory, failMessage)

        val jar = libDir.listFiles(ANNOTATIONS_JAR_FILTER)?.firstOrNull() ?: testServices.assertions.fail(failMessage)
        configuration.addJvmClasspathRoot(jar)
    }

    private val failMessage = { "Runtime JAR does not exist. Please run :z2-core:shadowJar" }
}