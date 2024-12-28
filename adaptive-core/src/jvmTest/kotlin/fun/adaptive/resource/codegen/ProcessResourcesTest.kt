package `fun`.adaptive.resource.codegen

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.resource.file.FileResourceSet
import `fun`.adaptive.resource.file.Files
import `fun`.adaptive.utility.DangerousApi
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.read
import `fun`.adaptive.utility.resolve
import kotlinx.io.files.Path
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test
import kotlin.test.assertEquals

class ProcessResourcesTest {

    val testFqn = "fun.adaptive.resource.codegen.ProcessResourcesTest"
    val testBase = Path("./src/jvmTest/kotlin/fun/adaptive/resource/codegen")

    @OptIn(ExperimentalCompilerApi::class)
    @Test
    fun test1() {
        val generatedSources = clearedTestPath("$testFqn.test1/generatedSources")
        val preparedResources = clearedTestPath("$testFqn.test1/preparedResources")

        @OptIn(DangerousApi::class) // clearedTestPath confines delete into test working directory
        processResources(
            sourcePath = testBase.resolve("set1"),
            packageName = "`fun`.adaptive.resource.codegen.test",
            kmpSourceSet = "jvmTest",
            generatedCodePath = generatedSources,
            preparedResourcesPath = preparedResources
        )

        val sourceFile = generatedSources.resolve("jvmTestFile0.kt")
        val sourceCode = sourceFile.read().decodeToString()
        val kotlinSource = SourceFile.kotlin("jvmTestFile0.kt", sourceCode)

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        assertEquals(ExitCode.OK, result.exitCode)
        val kClazz = result.classLoader.loadClass("fun.adaptive.resource.codegen.test.JvmTestFile0Kt")
        kClazz.methods.forEach { println(it) }

        val setA = kClazz.getMethod("getA", Files::class.java).invoke(null, Files)
        val setB = kClazz.getMethod("getB", Files::class.java).invoke(null, Files)

        assertEquals(
            FileResourceSet(
                name = "a",
                FileResource("/a-file.txt", setOf())
            ),
            setA
        )

        assertEquals(
            FileResourceSet(
                name = "b",
                FileResource("/b-file-hu-HU-mdpi-light.txt", setOf(LanguageQualifier("hu"), RegionQualifier("HU"), DensityQualifier.MDPI, ThemeQualifier.LIGHT)),
                FileResource("/b-file.txt", setOf())
            ),
            setB
        )
    }

}