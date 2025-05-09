package `fun`.adaptive.wireformat.json

import `fun`.adaptive.wireformat.json.elements.*
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonFormatTest {

    @Test
    fun testPrettyString_Null() {
        assertEquals("null", JsonNull().asPrettyString)
    }

    @Test
    fun testPrettyString_Number() {
        assertEquals("42", JsonNumber("42").asPrettyString)
    }

    @Test
    fun testPrettyString_String() {
        assertEquals("\"hello\"", JsonString("hello").asPrettyString)
    }

    @Test
    fun testPrettyString_Boolean() {
        assertEquals("true", JsonBoolean(true).asPrettyString)
    }

    @Test
    fun testPrettyString_Array() {

        val jsonArray = JsonArray().apply { this.value += listOf(JsonNumber("1"), JsonString("two"), JsonBoolean(false)) }

        val expected = """
            [
              1,
              "two",
              false
            ]
        """.trimIndent()

        assertEquals(expected, jsonArray.asPrettyString)
    }

    @Test
    fun testPrettyString_Object() {

        val jsonObject = JsonObject().apply {
            value +=
                mapOf(
                    "key1" to JsonNumber("123"),
                    "key2" to JsonString("value"),
                    "key3" to JsonBoolean(false)
                )
        }

        val expected = """
            {
              "key1": 123,
              "key2": "value",
              "key3": false
            }
        """.trimIndent()

        assertEquals(expected, jsonObject.asPrettyString)
    }

    @Test
    fun testPrettyString_NestedStructure() {

        val jsonObject = JsonObject().apply {
            value += mapOf(
                "array" to JsonArray().apply {
                    this.value +=
                        listOf(
                            JsonNumber("1"),
                            JsonObject().apply { this.value += mapOf("nestedKey" to JsonString("nestedValue")) }
                        )
                },
                "boolean" to JsonBoolean(true)
            )
        }

        val expected = """
            {
              "array": [
                1,
                {
                  "nestedKey": "nestedValue"
                }
              ],
              "boolean": true
            }
        """.trimIndent()

        assertEquals(expected, jsonObject.asPrettyString)
    }
}