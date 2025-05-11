package `fun`.adaptive.grove.doc

import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.*
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GroveDocCompilerTest {

    class TestSupport(testPath: Path) {
        val inPath = testPath.resolve("in").ensure()
        val defPath = inPath.resolve("definitions").ensure()
        val guidePath = inPath.resolve("guides").ensure()
        val outPath = testPath.resolve("out").ensure()

        val compilation = GroveDocCompilation().also {
            it.inPath = inPath
            it.outPath = outPath
        }

        val compiler = GroveDocCompiler(compilation)
    }

    fun compilerTest(testPath: Path, testFun: TestSupport.() -> Unit) {
        TestSupport(testPath).apply { testFun() }
    }

    @Test
    fun `test compilation with empty directory`() = compilerTest(clearedTestPath()) {
        assertTrue(compilation.notifications.isEmpty())
        assertTrue(outPath.isEmpty())
    }

    @Test
    fun `test processing single markdown file`() = compilerTest(clearedTestPath()) {
        val content = "# Test Header\nTest content"
        guidePath.resolve("test.md").write(content)

        compiler.compile()

        // the compilation reformats the MD file
        val expected = """
            # Test Header

            Test content
        """.trimIndent()

        val outMdPath = outPath.resolve("test.md")
        assertTrue(outMdPath.exists())
        assertEquals(expected, outMdPath.readString())
    }

    @Test
    fun `test processing multiple files in collection`() = compilerTest(clearedTestPath()) {
        val file1 = guidePath.resolve("test1.md")
        val file2 = guidePath.resolve("test2.md")

        file1.write("# Test 1")
        file2.write("# Test 2")

        compiler.compile()

        assertTrue(outPath.resolve("test1.md").exists())
        assertTrue(outPath.resolve("test2.md").exists())
    }

    @Test
    fun `test markdown transformation`() = compilerTest(clearedTestPath()) {
        val content = """
            # Test Header
            [Some Doc](guide://)
            """.trimIndent()

        val testFile = inPath.resolve("test.md")
        testFile.write(content)

        val someDocFile = guidePath.resolve("some doc.md")
        someDocFile.write("# Some Doc")

        compiler.compile()

        val outputPath = outPath.resolve("test.md")
        assertTrue(outputPath.exists())

        val processedContent = outputPath.readString()
        assertTrue(processedContent.contains("# Test Header"))
        assertTrue(processedContent.contains("[Some Doc](some%20doc.md)"))
    }

    @Test
    fun `test file collector reports collisions`() = compilerTest(clearedTestPath()) {
        // Create two files with the same normalized name
        val file1 = inPath.resolve("a").ensure().resolve("test.md")
        val file2 = inPath.resolve("b").ensure().resolve("TEST.md")

        file1.write("Content 1")
        file2.write("Content 2")

        compiler.compile()

        assertTrue(compiler.compilation.notifications.any {
            it.level == LogLevel.Warning && it.message.contains("collision")
        })
    }
}
