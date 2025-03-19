package `fun`.adaptive.resource.codegen

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import `fun`.adaptive.resource.*
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.resource.file.FileResourceSet
import `fun`.adaptive.resource.file.Files
import `fun`.adaptive.resource.font.FontResource
import `fun`.adaptive.resource.font.FontResourceSet
import `fun`.adaptive.resource.font.Fonts
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResource
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.image.ImageResource
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.resource.image.Images
import `fun`.adaptive.resource.string.StringStoreResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.utility.*
import kotlinx.coroutines.runBlocking
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
        val pathQualifier = "/fun.adaptive.resource.codegen.test"

        @OptIn(DangerousApi::class) // clearedTestPath confines delete into test working directory
        ResourceCompilation(
            originalResourcesPath = testBase.resolve("set1"),
            packageName = "`fun`.adaptive.resource.codegen.test",
            kmpSourceSet = "jvmTest",
            generatedCodePath = generatedSources,
            preparedResourcesPath = preparedResources
        ).compile()

        assertResources(generatedSources, "jvmTestFile0") {
            listOf(
                FileResourceSet(
                    name = "a",
                    FileResource("$pathQualifier/a-file.txt", setOf())
                ),
                FileResourceSet(
                    name = "b",
                    FileResource("$pathQualifier/b-file-hu-HU-mdpi-light.txt", setOf(LanguageQualifier("hu"), RegionQualifier("HU"), DensityQualifier.MDPI, ThemeQualifier.LIGHT)),
                    FileResource("$pathQualifier/b-file.txt", setOf())
                )
            )
        }

        assertResource(generatedSources, "jvmTestImage0") {
            ImageResourceSet(
                name = "img1",
                ImageResource("$pathQualifier/images/img1.txt", setOf())
            )
        }

        assertResource(generatedSources, "jvmTestFont0") {
            FontResourceSet(
                name = "e",
                FontResource("$pathQualifier/fonts/e.ttf", setOf())
            )
        }

        assertResource(generatedSources, "jvmTestGraphics0") {
            GraphicsResourceSet(
                name = "add",
                GraphicsResource("$pathQualifier/graphics/add.svg", setOf())
            )
        }

        assertStringResources(
            generatedSources, preparedResources, "jvmTestStringsStringStore0",
            ResourceEnvironment(LanguageQualifier("en"), RegionQualifier("US"), ThemeQualifier.INVALID, DensityQualifier.INVALID)
        ) {
            listOf(
                "app_name" to "Good Morning"
            )
        }

        assertStringResources(
            generatedSources, preparedResources, "jvmTestStringsStringStore0",
            ResourceEnvironment(LanguageQualifier("hu"), RegionQualifier("HU"), ThemeQualifier.INVALID, DensityQualifier.INVALID)
        ) {
            listOf(
                "app_name" to "Jó reggelt!"
            )
        }
    }

    fun assertResource(sourceDir: Path, name: String, expected: () -> ResourceFileSet<*>) {
        assertResources(sourceDir, name) { listOf(expected()) }
    }

    @OptIn(ExperimentalCompilerApi::class)
    fun assertResources(
        sourceDir: Path,
        name: String,
        expectedFun: () -> List<ResourceFileSet<*>>
    ) {
        val result = compileFile(sourceDir.resolve("$name.kt"))

        val expected = expectedFun()
        val classFqName = "fun.adaptive.resource.codegen.test.${name.uppercaseFirstChar()}Kt"
        val kClazz = result.classLoader.loadClass(classFqName)

        val globalAccessor = when (expected.first().type) {
            ResourceTypeQualifier.File -> Files
            ResourceTypeQualifier.Font -> Fonts
            ResourceTypeQualifier.Graphics -> Graphics
            ResourceTypeQualifier.Image -> Images
            ResourceTypeQualifier.Strings -> Strings
            ResourceTypeQualifier.Document -> Documents
        }

        expected.forEach {
            val set = kClazz.getMethod("get${it.name.uppercaseFirstChar()}", globalAccessor::class.java).invoke(null, globalAccessor)
            assertEquals(it, set)
        }
    }

    @OptIn(ExperimentalCompilerApi::class)
    fun assertStringResources(
        sourceDir: Path,
        preparedResources: Path,
        name: String,
        environment: ResourceEnvironment,
        expectedFun: () -> List<Pair<String, String>>
    ) {
        val result = compileFile(sourceDir.resolve("$name.kt"))

        val expected = expectedFun()
        val classFqName = "fun.adaptive.resource.codegen.test.${name.uppercaseFirstChar()}Kt"
        val kClazz = result.classLoader.loadClass(classFqName)

        val store = kClazz.getMethod("get${name.uppercaseFirstChar()}").invoke(null) as StringStoreResourceSet
        runBlocking {
            store.load(
                environment = environment,
                resourceReader = TestResourceReader { preparedResources.resolve(it.removePrefix("/fun.adaptive.resource.codegen.test")).read() }
            )
        }

        expected.forEach {
            val value = kClazz.getMethod("get${it.first.uppercaseFirstChar()}", Strings::class.java).invoke(null, Strings)
            assertEquals(it.second, value as String)
        }
    }

    @OptIn(ExperimentalCompilerApi::class)
    fun compileFile(sourceFile: Path): JvmCompilationResult {
        val sourceCode = sourceFile.read().decodeToString()
        val kotlinSource = SourceFile.kotlin(sourceFile.name, sourceCode)

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        assertEquals(ExitCode.OK, result.exitCode)

        return result
    }
}