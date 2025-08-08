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
        val humanReadable = testPath.resolve("out/human-readable").ensure()
        val training = testPath.resolve("out/separated").ensure()
        val plain = testPath.resolve("out/plain").ensure()
        val merged = testPath.resolve("out/merged").ensure()

        val valueServer = embeddedValueServer(FilePersistence(testPath.resolve("out/values").ensure())) { }

        val compilation = GroveDocCompilation(
            inPath = inPath,
            mdOutPath = testPath.resolve("out"),
            values = valueServer.serverWorker
        )

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
        assertTrue(plain.isEmpty())
        // human-readable and merged are disabled by default; their dirs may exist but should remain empty
        assertTrue(humanReadable.isEmpty())
        assertTrue(merged.isEmpty())
    }

    @Test
    @JsName("testProcessingSingleMarkdownFile")
    fun `test processing single markdown file`() = compilerTest(clearedTestPath()) {
        val content = "# Test Header\nTest content"
        guidePath.resolve("test.md").write(content)

        compiler.compile()

        // the compilation reformats the MD file
        val expectedPlain = """
            # Test Header

            Test content
        """.trimIndent()

        val expectedTraining = """
            <!-- name: test.md -->
            <!-- type: guide -->

            # Test Header

            Test content


        """.trimIndent()

        val outPlainPath = plain.resolve("guide-test.md")
        val trainingPath = compilation.outPathTrainingSeparated.resolve("guide-test.md")
        assertTrue(outPlainPath.exists())
        assertTrue(trainingPath.exists())
        assertEquals(expectedPlain, outPlainPath.readString())
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

        assertTrue(compilation.outPathTrainingSeparated.resolve("guide-test1.md").exists())
        assertTrue(compilation.outPathTrainingSeparated.resolve("guide-test2.md").exists())
        assertTrue(plain.resolve("guide-test1.md").exists())
        assertTrue(plain.resolve("guide-test2.md").exists())
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

        val trainingPath = compilation.outPathTrainingSeparated.resolve("guide-test.md")
        val plainPath = compilation.outPathPlain.resolve("guide-test.md")
        assertTrue(trainingPath.exists())
        assertTrue(plainPath.exists())

        val processedTrainingContent = trainingPath.readString()
        val processedPlainContent = plainPath.readString()
        assertTrue(processedTrainingContent.contains("# Test Header"))
        assertTrue(processedTrainingContent.contains("[Some Doc](guide://)"))
        assertTrue(processedPlainContent.contains("# Test Header"))
        // [DEBUG_LOG] print plain content for inspection
        println("[DEBUG_LOG] PLAIN OUT: \n" + processedPlainContent)
        // In plain mode the link should become a relative link to the plain output file in the same directory
        assertTrue(processedPlainContent.contains("[Some Doc](guide-some%20doc.md)"))
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
        val content = "[acl](property://AvValue)"
        inPath.resolve("AvValue.kt").write("class AvValue(val value: Int)")
        guidePath.resolve("test.md").write(content)

        compiler.compile()
        assertTrue(compiler.notifications.isEmpty())
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

        val plainOut = plain.resolve("guide-eg.md").readString()
        val trainingOut = compilation.outPathTrainingSeparated.resolve("guide-eg.md").readString()

        assertTrue(plainOut.contains("import some.package.A"), "Plain should include imports for example-group")
        assertTrue(plainOut.contains("import some.other.B"))
        assertTrue(trainingOut.contains("import some.package.A"), "Training should include imports in example-group")
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

        val plainOut = plain.resolve("guide-ex.md").readString()
        val trainingOut = compilation.outPathTrainingSeparated.resolve("guide-ex.md").readString()

        assertTrue(plainOut.contains("import x.y.Z"))
        assertTrue(trainingOut.contains("import x.y.Z"))
    }
}
