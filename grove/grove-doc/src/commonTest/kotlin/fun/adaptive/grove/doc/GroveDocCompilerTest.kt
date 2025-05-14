package `fun`.adaptive.grove.doc

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.*
import kotlinx.io.files.Path
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GroveDocCompilerTest {

    class TestSupport(testPath: Path) {
        val inPath = testPath.resolve("in").ensure()
        val defPath = inPath.resolve("definitions").ensure()
        val guidePath = inPath.resolve("guides").ensure()
        val humanReadable = testPath.resolve("out/human-readable").ensure()
        val training = testPath.resolve("out/training").ensure()
        val merged = testPath.resolve("out/training-merged.md")

        val compilation = GroveDocCompilation().also {
            it.inPath = inPath
            it.outPathHumanReadable = humanReadable
            it.outPathAITraining = training
            it.outPathAIMerged = merged
        }

        val compiler = GroveDocCompiler(compilation)
    }

    fun compilerTest(testPath: Path, testFun: TestSupport.() -> Unit) {
        TestSupport(testPath).apply { testFun() }
    }

    @Test
    @JsName("testCompilationWithEmptyDirectory")
    fun `test compilation with empty directory`() = compilerTest(clearedTestPath()) {
        assertTrue(compilation.notifications.isEmpty())
        assertTrue(training.isEmpty())
        assertTrue(humanReadable.isEmpty())
    }

    @Test
    @JsName("testProcessingSingleMarkdownFile")
    fun `test processing single markdown file`() = compilerTest(clearedTestPath()) {
        val content = "# Test Header\nTest content"
        guidePath.resolve("test.md").write(content)

        compiler.compile()

        // the compilation reformats the MD file
        val expectedHumanReadable = """
            # Test Header

            Test content
        """.trimIndent()

        val expectedTraining = """
            <!-- name: test.md -->
            <!-- type: guide -->

            # Test Header

            Test content


        """.trimIndent()

        val outMdPath = humanReadable.resolve("test.md")
        val trainingPath = training.resolve("test.md")
        assertTrue(outMdPath.exists())
        assertTrue(trainingPath.exists())
        assertEquals(expectedHumanReadable, outMdPath.readString())
        assertEquals(expectedTraining, trainingPath.readString())
    }

    @Test
    @JsName("testProcessingMultipleFilesInCollection")
    fun `test processing multiple files in collection`() = compilerTest(clearedTestPath()) {
        val file1 = guidePath.resolve("test1.md")
        val file2 = guidePath.resolve("test2.md")

        file1.write("# Test 1")
        file2.write("# Test 2")

        compiler.compile()

        assertTrue(training.resolve("test1.md").exists())
        assertTrue(training.resolve("test2.md").exists())
        assertTrue(humanReadable.resolve("test1.md").exists())
        assertTrue(humanReadable.resolve("test2.md").exists())
    }

    @Test
    @JsName("testMarkdownTransformation")
    fun `test markdown transformation`() = compilerTest(clearedTestPath()) {
        val content = """
            # Test Header
            [Some Doc](guide://)
            """.trimIndent()

        val testFile = guidePath.resolve("test.md")
        testFile.write(content)

        val someDocFile = guidePath.resolve("some doc.md")
        someDocFile.write("# Some Doc")

        compiler.compile()

        val trainingPath = training.resolve("test.md")
        val humanReadablePath = humanReadable.resolve("test.md")
        assertTrue(trainingPath.exists())
        assertTrue(humanReadablePath.exists())

        val processedTrainingContent = trainingPath.readString()
        val processedHumanContent = humanReadablePath.readString()
        assertTrue(processedTrainingContent.contains("# Test Header"))
        assertTrue(processedTrainingContent.contains("[Some Doc](guide://)"))
        assertTrue(processedHumanContent.contains("# Test Header"))
        assertTrue(processedHumanContent.contains("[Some Doc](some%20doc.md)"))
    }

    @Test
    @JsName("testFileCollectorReportsCollisions")
    fun `test file collector reports collisions`() = compilerTest(clearedTestPath()) {
        // Create two files with the same normalized name
        val file1 = guidePath.resolve("a").ensure().resolve("test.md")
        val file2 = guidePath.resolve("b").ensure().resolve("TEST.md")

        file1.write("Content 1")
        file2.write("Content 2")

        compiler.collect()

        assertTrue(compiler.compilation.notifications.any {
            it.level == LogLevel.Warning && it.message.contains("collision")
        })
    }

    @Test
    @JsName("adhocTest")
    fun `adhoc test`() = compilerTest(clearedTestPath()) {
        val content = "[acl](property://AvValue)"
        guidePath.resolve("test.md").write(content)

        compiler.compile()
    }
}
