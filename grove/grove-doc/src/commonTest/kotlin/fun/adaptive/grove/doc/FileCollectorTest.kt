package `fun`.adaptive.grove.doc

import `fun`.adaptive.grove.doc.lib.compiler.FileCollector
import `fun`.adaptive.grove.doc.lib.compiler.GroveDocCompilation
import `fun`.adaptive.persistence.clearedTestPath
import `fun`.adaptive.persistence.ensure
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.write
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path
import kotlin.test.*
import kotlin.js.JsName

class FileCollectorTest {

    internal fun fileCollectorTest(testPath: Path, testFun: (GroveDocCompilation, FileCollector) -> Unit) {
        val valueServer = embeddedValueServer(FilePersistence(testPath.resolve("out/values").ensure())) { }

        val compilation = GroveDocCompilation(
            testPath.resolve("in").ensure(),
            testPath.resolve("out").ensure(),
            valueServer.serverWorker
        )
        val collector = FileCollector(compilation)

        // Setup test files
        collector.ktFiles["MyClass"] = mutableListOf(
            Path("src/main/kotlin/com/example/MyClass.kt")
        )
        collector.ktFiles["DuplicateClass"] = mutableListOf(
            Path("src/main/kotlin/com/example/one/DuplicateClass.kt"),
            Path("src/main/kotlin/com/example/two/DuplicateClass.kt")
        )
        collector.ktFiles["MyScope"] = mutableListOf(
            Path("src/main/kotlin/com/example/MyScope.kt")
        )

        testFun(compilation, collector)
    }

    @Test
    @JsName("testClassLookupWithoutScope")
    fun `test class lookup without scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("class", "MyClass", null)
        assertNotNull(result)
        assertEquals("src/main/kotlin/com/example/MyClass.kt", result.toString())
    }

    @Test
    @JsName("testClassLookupWithScope")
    fun `test class lookup with scope`()  = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode(
            "class",
            "DuplicateClass",
            "com.example.one"
        )
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/one/DuplicateClass.kt",
            result.toString()
        )
    }

    @Test
    @JsName("testClassLookupWithNonExistentClass")
    fun `test class lookup with non-existent class`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("class", "NonExistentClass", null)
        assertNull(result)
    }

    @Test
    @JsName("testFunctionLookupWithoutScope")
    fun `test function lookup without scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("function", "myFunction", null)
        assertNull(result)
        assertFalse(compilation.notifications.isEmpty())
        assertTrue(compilation.notifications.any { it.message.contains("myFunction") })
    }

    @Test
    @JsName("testFunctionLookupWithValidScope")
    fun `test function lookup with valid scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("function", "myFunction", "MyScope")
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/MyScope.kt",
            result.toString()
        )
    }

    @Test
    @JsName("testFunctionLookupWithInvalidScope")
    fun `test function lookup with invalid scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("function", "myFunction", "NonExistentScope")
        assertNull(result)
        assertFalse(compilation.notifications.isEmpty())
        assertTrue(compilation.notifications.any { it.message.contains("myFunction") })
    }

    @Test
    @JsName("testPropertyLookupWithValidScope")
    fun `test property lookup with valid scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("property", "myProperty", "MyScope")
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/MyScope.kt",
            result.toString()
        )
    }

    @Test
    @JsName("testLookupWithInvalidScheme")
    fun `test lookup with invalid scheme`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("invalid", "name", "scope")
        assertNull(result)
    }

    @Test
    @JsName("testCollectSubprojectsFromSettings_basic")
    fun `test collectSubprojectsFromSettings basic`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val settings = """
            includeBuild("core/core-core")
            includeBuild('lib/lib-auth')
            includeBuild("grove/grove-doc")
            includeBuild("grove/grove-doc") // duplicate
        """.trimIndent()
        compilation.inPath.resolve("settings.gradle.kts").write(settings, overwrite = true)

        val subs = collector.collectSubprojectsFromSettings()
        assertEquals(3, subs.size)

        val names = subs.map { it.name }.toSet()
        assertTrue(names.contains("core-core"))
        assertTrue(names.contains("lib-auth"))
        assertTrue(names.contains("grove-doc"))

        val byName = subs.associateBy { it.name }
        assertEquals("core/core-core", byName["core-core"]?.relativePath)
        assertEquals("lib/lib-auth", byName["lib-auth"]?.relativePath)
        assertEquals("grove/grove-doc", byName["grove-doc"]?.relativePath)

        assertEquals(compilation.inPath.resolve("core/core-core").toString(), byName["core-core"]?.path?.toString())
        assertEquals(compilation.inPath.resolve("lib/lib-auth").toString(), byName["lib-auth"]?.path?.toString())
        assertEquals(compilation.inPath.resolve("grove/grove-doc").toString(), byName["grove-doc"]?.path?.toString())
    }

    @Test
    @JsName("testCollectSubprojectsFromSettings_missingFile")
    fun `test collectSubprojectsFromSettings missing file`() = fileCollectorTest(clearedTestPath()) { _, collector ->
        val subs = collector.collectSubprojectsFromSettings()
        assertTrue(subs.isEmpty())
    }
}