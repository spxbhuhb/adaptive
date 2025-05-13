package `fun`.adaptive.resource.codegen

import `fun`.adaptive.runtime.GlobalRuntimeContext
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
        }?.also {
            val (resourceCompilation, values) = it

            assertTrue(resourceCompilation.reports.isEmpty())
            assertEquals(4, values.size)

            assertTrue(values.containsKey("key1"))
            assertTrue(values.containsKey("key2"))
            assertTrue(values.containsKey("key3"))
            assertTrue(values.containsKey("key4"))

            assertEquals("key1" to "Value1", values["key1"] !!.value.decompose())
            assertEquals("key2" to "Value2", values["key2"] !!.value.decompose())
            assertEquals("key3" to "", values["key3"] !!.value.decompose())
            assertEquals("key4" to "", values["key4"] !!.value.decompose())
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
        }?.also {
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
        }?.also {
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
        }?.also {
            val (resourceCompilation, values) = it

            assertTrue(resourceCompilation.reports.isNotEmpty()) // Expect warnings or errors due to duplicate keys
            assertEquals(1, values.size)
            assertTrue(values.containsKey("key1"))

            val (key, value) = values["key1"] !!.value.decompose()

            assertEquals("key1", key) // The first value should be preserved
            assertEquals("Value1", value) // The first value should be preserved
        }
    }

    fun ByteArray.decompose(): Pair<String, String> {
        val keySize = this[0].toInt()
        val key = this.decodeToString(1, keySize + 1)

        val valueOffset = keySize + 1
        val value = this.decodeToString(valueOffset, this.size)

        return key to value
    }

    fun test(xmlContent: () -> String): Pair<ResourceCompilation, Map<String, ResourceCompilation.ResourceValue>>? {
        if (GlobalRuntimeContext.platform.isJs) return null

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