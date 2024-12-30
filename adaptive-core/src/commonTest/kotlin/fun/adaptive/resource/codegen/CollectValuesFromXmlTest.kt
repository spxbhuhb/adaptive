package `fun`.adaptive.resource.codegen

import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.testPath
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectValuesFromXmlTest {

    @Test
    fun testCollectValuesFromXml() {
        test {
            """
                <resources>
                    <string name="key1">Value1</string>
                    <string name="key2">Value2</string>
                    <string name="key3"></string>
                    <string name="key4"/>
                </resources>
            """.trimIndent()
        }.also {
            val (resourceCompilation, values) = it

            assertTrue(resourceCompilation.reports.isEmpty())
            assertEquals(4, values.size)
            assertTrue(values.containsKey("key1"))
            assertTrue(values.containsKey("key2"))
            assertTrue(values.containsKey("key3"))
            assertTrue(values.containsKey("key4"))
            assertEquals("Value1", values["key1"] !!.value.decodeToString())
            assertEquals("Value2", values["key2"] !!.value.decodeToString())
            assertEquals("", values["key3"] !!.value.decodeToString())
            assertEquals("", values["key4"] !!.value.decodeToString())
        }
    }

    @Test
    fun testCollectValuesFromXmlWithInvalidXml() {
        test {
            """
            <resources>
                <invalid name="key1">Value1</invalid>
            </resources>
        """.trimIndent()
        }.also {
            val (resourceCompilation, values) = it

            assertTrue(resourceCompilation.reports.isNotEmpty())
            assertTrue(values.isEmpty()) // No valid entries should be parsed in this case
        }

    }

    @Test
    fun testCollectValuesFromXmlWithEmptyResources() {
        test {
            """
            <resources>
            </resources>
        """.trimIndent()
        }.also {
            val (resourceCompilation, values) = it

            assertTrue(resourceCompilation.reports.isEmpty())
            assertTrue(values.isEmpty()) // Ensure no values are extracted from an empty <resources> block
        }
    }

    @Test
    fun testCollectValuesFromXmlWithDuplicateKeys() {
        test {
            """
            <resources>
                <string name="key1">Value1</string>
                <string name="key1">Value2</string>
            </resources>
        """.trimIndent()
        }.also {
            val (resourceCompilation, values) = it

            assertTrue(resourceCompilation.reports.isNotEmpty()) // Expect warnings or errors due to duplicate keys
            assertEquals(1, values.size)
            assertTrue(values.containsKey("key1"))
            assertEquals("Value1", values["key1"] !!.value.decodeToString()) // The first value should be preserved
        }
    }

    fun test(xmlContent: () -> String): Pair<ResourceCompilation, Map<String, ResourceCompilation.ResourceValue>> {
        val sourcePath = testPath.resolve("resources")
        val filePath = Path("/fake/path/to/file.xml")

        val values = mutableMapOf<String, ResourceCompilation.ResourceValue>()

        val resourceCompilation = ResourceCompilation(
            sourcePath, "", "commonTest", sourcePath, sourcePath
        )

        resourceCompilation.collectValuesFromXml(filePath, xmlContent(), values)

        return resourceCompilation to values
    }
}