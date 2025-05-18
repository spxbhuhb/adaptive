package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.codegen.ResourceCompilation.FileAndValues
import `fun`.adaptive.resource.codegen.ResourceCompilation.ResourceValue
import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.globalTestPath
import kotlinx.io.files.Path
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IndexValueSetsTest {

    @Test
    @JsName("indexValueSetsReturnsNullForMismatchedKeys")
    fun `indexValueSets returns null for mismatched keys reports are not empty`() {
        test(
            """
                <resources>
                    <string name="key1">Value1</string>
                    <string name="key2">Value2</string>
                </resources>
            """.trimIndent(),
            """
                <resources>
                    <string name="key1">Value1</string>
                </resources>
            """.trimIndent()
        ) { sets ->
            val result = indexValueSets(sets)
            assertTrue { reports.isNotEmpty() }
            assertNull(result, "Expected null as the keys don't match.")
        }
    }

    @Test
    @JsName("indexValueSetsReturnsSortedListWhenKeysMatch")
    fun `indexValueSets returns sorted list when keys match`() {
        test(
            """
                <resources>
                    <string name="key1">Value1</string>
                    <string name="key2">Value2</string>
                </resources>
            """.trimIndent(),
            """
                <resources>
                    <string name="key2">Value2</string>
                    <string name="key1">Value1</string>
                </resources>
            """.trimIndent()
        ) { sets ->
            val result = indexValueSets(sets)
            assertTrue { reports.isEmpty() }
            assertEquals(listOf("key1", "key2"), result, "Expected sorted list of keys.")
        }
    }

    @Test
    @JsName("indexValueSetsHandlesEmptyInput")
    fun `indexValueSets handles empty sets`() {
        test { sets ->
            val result = indexValueSets(sets)
            assertTrue { reports.isEmpty() }
            assertEquals(emptyList(), result, "Expected empty list for empty input.")
        }
    }

    fun test(
        vararg sources: String,
        testFun: ResourceCompilation.(sets: List<FileAndValues>) -> Unit
    ) {
        if (GlobalRuntimeContext.platform.isJs) return

        val sourcePath = globalTestPath.resolve("resources")
        val filePath = Path("/fake/path/to/file.xml")

        val resourceCompilation = ResourceCompilation(
            sourcePath, "", "commonTest", sourcePath, sourcePath,
        )

        val sets = sources.map {
            FileAndValues(
                FileResource(filePath.toString(), emptySet()),
                mutableMapOf<String, ResourceValue>().also { values ->
                    resourceCompilation.collectValuesFromXml(filePath, it, values)
                }
            )
        }

        assertTrue { resourceCompilation.reports.isEmpty() }

        resourceCompilation.testFun(sets)
    }

}