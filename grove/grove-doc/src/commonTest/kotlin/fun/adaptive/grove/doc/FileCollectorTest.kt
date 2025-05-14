package `fun`.adaptive.grove.doc

import kotlinx.io.files.Path
import kotlin.test.*

class FileCollectorTest {
    private lateinit var compilation: GroveDocCompilation
    private lateinit var collector: FileCollector

    @BeforeTest
    fun setup() {
        compilation = GroveDocCompilation()
        collector = FileCollector(compilation)

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
    }

    @Test
    fun `test class lookup without scope`() {
        val result = collector.lookupCode("class", "MyClass", null)
        assertNotNull(result)
        assertEquals("src/main/kotlin/com/example/MyClass.kt", result.toString())
    }

    @Test
    fun `test class lookup with scope`() {
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
    fun `test class lookup with non-existent class`() {
        val result = collector.lookupCode("class", "NonExistentClass", null)
        assertNull(result)
    }

    @Test
    fun `test function lookup without scope`() {
        val result = collector.lookupCode("function", "myFunction", null)
        assertNull(result)
        assertFalse(compilation.notifications.isEmpty())
        assertTrue(compilation.notifications.any { it.message.contains("myFunction") })
    }

    @Test
    fun `test function lookup with valid scope`() {
        val result = collector.lookupCode("function", "myFunction", "MyScope")
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/MyScope.kt",
            result.toString()
        )
    }

    @Test
    fun `test function lookup with invalid scope`() {
        val result = collector.lookupCode("function", "myFunction", "NonExistentScope")
        assertNull(result)
        assertFalse(compilation.notifications.isEmpty())
        assertTrue(compilation.notifications.any { it.message.contains("myFunction") })
    }

    @Test
    fun `test property lookup with valid scope`() {
        val result = collector.lookupCode("property", "myProperty", "MyScope")
        assertNotNull(result)
        assertEquals(
            "src/main/kotlin/com/example/MyScope.kt",
            result.toString()
        )
    }

    @Test
    fun `test lookup with invalid scheme`() {
        val result = collector.lookupCode("invalid", "name", "scope")
        assertNull(result)
    }
}