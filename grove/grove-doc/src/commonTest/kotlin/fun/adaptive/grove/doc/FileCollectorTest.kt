package `fun`.adaptive.grove.doc

import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.resolve
import kotlinx.io.files.Path
import kotlin.test.*

class FileCollectorTest {

    internal fun fileCollectorTest(testPath: Path, testFun: (GroveDocCompilation, FileCollector) -> Unit) {
        val compilation = GroveDocCompilation(
            testPath.resolve("in").ensure(),
            testPath.resolve("out").ensure()
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
    fun `test class lookup without scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("class", "MyClass", null)
        assertNotNull(result)
        assertEquals("src/main/kotlin/com/example/MyClass.kt", result.toString())
    }

    @Test
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
    fun `test class lookup with non-existent class`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("class", "NonExistentClass", null)
        assertNull(result)
    }

    @Test
    fun `test function lookup without scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("function", "myFunction", null)
        assertNull(result)
        assertFalse(compilation.notifications.isEmpty())
        assertTrue(compilation.notifications.any { it.message.contains("myFunction") })
    }

    @Test
    fun `test function lookup with valid scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("function", "myFunction", "MyScope")
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/MyScope.kt",
            result.toString()
        )
    }

    @Test
    fun `test function lookup with invalid scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("function", "myFunction", "NonExistentScope")
        assertNull(result)
        assertFalse(compilation.notifications.isEmpty())
        assertTrue(compilation.notifications.any { it.message.contains("myFunction") })
    }

    @Test
    fun `test property lookup with valid scope`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("property", "myProperty", "MyScope")
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/MyScope.kt",
            result.toString()
        )
    }

    @Test
    fun `test lookup with invalid scheme`() = fileCollectorTest(clearedTestPath()) { compilation, collector ->
        val result = collector.lookupCode("invalid", "name", "scope")
        assertNull(result)
    }
}