package `fun`.adaptive.grove.doc

import `fun`.adaptive.grove.doc.lib.compiler.GroveDocCompilation
import `fun`.adaptive.grove.doc.lib.compiler.GroveDocCompiler
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.persistence.*
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GroveDocCompilerTest {

    class TestSupport(testPath: Path) {
        val inPath = testPath.resolve("in").ensure()
        val guidePath = inPath.resolve("guides").ensure()
        val junieLocal = testPath.resolve("out/junie-local").ensure()
        val aiConsumer = testPath.resolve("out/ai-consumer").ensure()
        val site = testPath.resolve("out/site").ensure()

        val valueServer = embeddedValueServer(FilePersistence(testPath.resolve("out/values").ensure())) { }

        val compilation = GroveDocCompilation(
            inPath = inPath,
            outPath = testPath.resolve("out"),
            values = valueServer.serverWorker
        )

        val compiler = GroveDocCompiler(compilation)
    }

    fun compilerTest(testPath: Path, testFun: TestSupport.() -> Unit) {
        TestSupport(testPath).apply { testFun() }
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

        val outJunie = junieLocal.resolve("guides/test.md")
        val outAI = aiConsumer.resolve("guides/test.md")
        assertTrue(outJunie.exists())
        assertTrue(outAI.exists())
        assertEquals(expected, outJunie.readString())
        assertEquals(expected, outAI.readString())
    }

    @Test
    @JsName("testProcessingMultipleFilesInCollection")
    fun `test processing multiple files in collection`() = compilerTest(clearedTestPath()) {
        val file1 = guidePath.resolve("test1.md")
        val file2 = guidePath.resolve("test2.md")

        file1.write("# Test 1")
        file2.write("# Test 2")

        compiler.compile()

        assertTrue(junieLocal.resolve("guides/test1.md").exists())
        assertTrue(junieLocal.resolve("guides/test2.md").exists())
        assertTrue(aiConsumer.resolve("guides/test1.md").exists())
        assertTrue(aiConsumer.resolve("guides/test2.md").exists())
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

        val outJunie = junieLocal.resolve("guides/test.md")
        val outAI = aiConsumer.resolve("guides/test.md")
        assertTrue(outJunie.exists())
        assertTrue(outAI.exists())

        val junieContent = outJunie.readString()
        val aiContent = outAI.readString()
        assertTrue(junieContent.contains("# Test Header"))
        assertTrue(aiContent.contains("# Test Header"))
        // Link to guide should be resolved to relative output path in both targets using ../<type>/<name>
        assertTrue(junieContent.contains("[Some Doc](../guides/some doc.md)"))
        assertTrue(aiContent.contains("[Some Doc](../guides/some doc.md)"))
    }

    @Test
    @JsName("testFileCollectorReportsCollisions")
    fun `test file collector reports collisions`() = compilerTest(clearedTestPath()) {
        // Create two files with the same normalized name
        val file1 = guidePath.resolve("a").ensure().resolve("test.md")
        val file2 = guidePath.resolve("b").ensure().resolve("TEST.md")

        file1.write("Content 1")
        file2.write("Content 2")

        compiler.fileCollector.collectFiles(compilation.inPath)
        compiler.fileCollector.reportCollisions()

        assertTrue(compiler.compilation.notifications.any {
            it.level == LogLevel.Warning && it.message.contains("collision")
        })
    }

    @Test
    @JsName("adhocTest")
    fun `adhoc test`() = compilerTest(clearedTestPath()) {
        val content = "[AvValue](class://)"
        inPath.resolve("AvValue.kt").write("class AvValue(val value: Int)")
        guidePath.resolve("test.md").write(content)

        compiler.compile()
        assertTrue(compiler.notifications.isEmpty())
        // Verify AI-Consumer output rewrites class:// to a GitHub URL
        val aiOut = aiConsumer.resolve("guides/test.md").readString()
        assertTrue(aiOut.contains("https://github.com/spxbhuhb/adaptive/tree/main/"))
        assertTrue(aiOut.contains("in/AvValue.kt"))
    }

    @Test
    @JsName("adhocTest2")
    fun `adhoc test2`() = compilerTest(clearedTestPath()) {
        val content = "[valueBlobUploadExample](example://)"
        inPath.resolve("valueBlobUploadExample.kt").write("// example of blob upload")
        guidePath.resolve("test.md").write(content)

        compiler.compile()
        assertTrue(compiler.notifications.isEmpty())
    }

    @Test
    @JsName("testGetExample")
    fun `test getExample method`() = compilerTest(clearedTestPath()) {
        // Create a test Kotlin file with a structure similar to the example file
        val exampleContent = """
            package test.example

            import some.package.Class1
            import some.package.Class2

            /**
             * # Example Name
             *
             * - First explanation point
             * - Second explanation point
             */
            @Annotation
            fun exampleFunction() {
                // Example code here
                val x = 42
                println(x)
            }
        """.trimIndent()

        val exampleFile = inPath.resolve("testExample.kt")
        exampleFile.write(exampleContent)

        // Call the getExample method
        val example = compiler.processExample(exampleFile)

        // Verify the extracted components
        assertEquals("Example Name", example.name)
        assertEquals("\n- First explanation point\n- Second explanation point", example.explanation)
        assertEquals("exampleFunction", example.fragmentKey)

        val expectedCode = """
            @Annotation
            fun exampleFunction() {
                // Example code here
                val x = 42
                println(x)
            }
        """.trimIndent()

        assertEquals(expectedCode, example.exampleCode)
        assertEquals(exampleContent, example.fullCode)
        assertTrue(example.repoPath.endsWith("testExample.kt"))

        // Imports should be collected as well
        assertEquals(listOf("import some.package.Class1", "import some.package.Class2"), example.imports)
    }
    @Test
    @JsName("testExampleFilesCollection")
    fun `test example files collection`() = compilerTest(clearedTestPath()) {
        // Create example files with the correct naming convention: <order>_<group>_<name>_example.kt
        val group1 = "button"
        val group2 = "input"

        // Create example files for group1
        val example1 = inPath.resolve("01_${group1}_basic_example.kt")
        val example2 = inPath.resolve("02_${group1}_advanced_example.kt")

        // Create example files for group2
        val example3 = inPath.resolve("01_${group2}_simple_example.kt")
        val example4 = inPath.resolve("02_${group2}_complex_example.kt")

        // Write content to the example files
        example1.write("""
            package test.example

            /**
             * # Basic Button Example
             * 
             * - This is a basic button example
             */
            fun basicButtonExample() {
                // Example code
            }
        """.trimIndent())

        example2.write("""
            package test.example

            /**
             * # Advanced Button Example
             * 
             * - This is an advanced button example
             */
            fun advancedButtonExample() {
                // Example code
            }
        """.trimIndent())

        example3.write("""
            package test.example

            /**
             * # Simple Input Example
             * 
             * - This is a simple input example
             */
            fun simpleInputExample() {
                // Example code
            }
        """.trimIndent())

        example4.write("""
            package test.example

            /**
             * # Complex Input Example
             * 
             * - This is a complex input example
             */
            fun complexInputExample() {
                // Example code
            }
        """.trimIndent())

        // Run the compiler's collect method
        compiler.fileCollector.collectFiles(compilation.inPath)
        compiler.fileCollector.reportCollisions()

        // Verify that the examples are collected properly in the examples map
        val examples = compilation.fileCollector.examples

        // Check that both groups are in the map
        assertTrue(examples.containsKey(group1), "Examples map should contain the '${group1}' group")
        assertTrue(examples.containsKey(group2), "Examples map should contain the '${group2}' group")

        // Check that each group has the correct number of examples
        assertEquals(2, examples[group1]?.size, "The '${group1}' group should have 2 examples")
        assertEquals(2, examples[group2]?.size, "The '${group2}' group should have 2 examples")

        // Check that the examples in each group are the correct files
        assertEquals(examples[group1]?.any { it.toString().endsWith("01_${group1}_basic_example.kt") }, true, "The '${group1}' group should contain the basic example")
        assertEquals(examples[group1]?.any { it.toString().endsWith("02_${group1}_advanced_example.kt") }, true, "The '${group1}' group should contain the advanced example")

        assertEquals(examples[group2]?.any { it.toString().endsWith("01_${group2}_simple_example.kt") }, true, "The '${group2}' group should contain the simple example")
        assertEquals(examples[group2]?.any { it.toString().endsWith("02_${group2}_complex_example.kt") }, true, "The '${group2}' group should contain the complex example")
        }

    @Test
    @JsName("testPlainExampleGroupIncludesImports")
    fun `test plain example-group includes imports`() = compilerTest(clearedTestPath()) {
        // Create an example file with imports following the naming convention
        val group = "sample"
        val exampleKt = inPath.resolve("01_${group}_withImports_example.kt")
        val exampleContent = """
            package test.example

            import some.package.A
            import some.other.B

            /**
             * # With imports
             *
             * - Shows that imports are included in Plain output
             */
            @Annotation
            fun withImportsExample() {
                println(A::class)
                println(B::class)
            }
        """.trimIndent()
        exampleKt.write(exampleContent)

        // Guide referencing the example group via actualize
        val guide = guidePath.resolve("eg.md")
        guide.write("[examples](actualize://example-group?name=$group)")

        compiler.compile()

        val junieOut = junieLocal.resolve("guides/eg.md").readString()
        val aiOut = aiConsumer.resolve("guides/eg.md").readString()

        assertTrue(junieOut.contains("import some.package.A"), "Junie-Local should include imports for example-group")
        assertTrue(junieOut.contains("import some.other.B"))
        assertTrue(aiOut.contains("import some.package.A"), "AI-Consumer should include imports in example-group")
    }

    @Test
    @JsName("testPlainScopedExampleLinkIncludesImports")
    fun `test plain scoped example link includes imports`() = compilerTest(clearedTestPath()) {
        // Create a Kotlin file with imports and a function
        val file = inPath.resolve("01_demo_basic_example.kt")
        val content = """
            package test.example

            import x.y.Z

            /**
             * # Demo
             */
            @Annotation
            fun basicExample() {
                println(Z::class)
            }
        """.trimIndent()
        file.write(content)

        // Guide referencing the specific function via example:// with scope
        val guide = guidePath.resolve("ex.md")
        guide.write("[basicExample](example://01_demo_basic_example)")

        compiler.compile()

        val junieOut = junieLocal.resolve("guides/ex.md").readString()
        val aiOut = aiConsumer.resolve("guides/ex.md").readString()

        // example:// link should expand to a code fence containing the function body (imports are not added for example links)
        assertTrue(junieOut.contains("fun basicExample()"))
        assertTrue(aiOut.contains("fun basicExample()"))
    }
}
