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
        val separated = testPath.resolve("out/separated").ensure()
        val merged = testPath.resolve("out/merged.md")

        val compilation = GroveDocCompilation().also {
            it.inPath = inPath
            it.outPathSeparated = separated
            it.outPathMerged = merged
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
        assertTrue(separated.isEmpty())
    }

    @Test
    @JsName("testProcessingSingleMarkdownFile")
    fun `test processing single markdown file`() = compilerTest(clearedTestPath()) {
        val content = "# Test Header\nTest content"
        guidePath.resolve("test.md").write(content)

        compiler.compile()

        // the compilation reformats the MD file
        val expected = """
            # Test Header

            Test content
        """.trimIndent()

        val outMdPath = separated.resolve("test.md")
        assertTrue(outMdPath.exists())
        assertEquals(expected, outMdPath.readString())
    }

    @Test
    @JsName("testProcessingMultipleFilesInCollection")
    fun `test processing multiple files in collection`() = compilerTest(clearedTestPath()) {
        val file1 = guidePath.resolve("test1.md")
        val file2 = guidePath.resolve("test2.md")

        file1.write("# Test 1")
        file2.write("# Test 2")

        compiler.compile()

        assertTrue(separated.resolve("test1.md").exists())
        assertTrue(separated.resolve("test2.md").exists())
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

        val outputPath = separated.resolve("test.md")
        assertTrue(outputPath.exists())

        val processedContent = outputPath.readString()
        assertTrue(processedContent.contains("# Test Header"))
        assertTrue(processedContent.contains("[Some Doc](some%20doc.md)"))
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
}
