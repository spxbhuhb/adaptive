package `fun`.adaptive.wireformat.json.visitor

import `fun`.adaptive.utility.*
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.elements.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TransformJsonTest {

    val input = """
        {
            "name": "test",
            "age": 12,
            "address": {
                "street": "test street",
                "number": 45
            }
        }
    """.trimIndent()

    @Test
    fun processJson() {
        val path = clearedTestPath().resolve("test.json")
        path.write(input)

        val root = path.readJson()
        assertIs<JsonObject>(root)

        val transformed = root.accept(object : JsonTransformerVoidWithContext() {
            override fun visitNumber(jsonNumber: JsonNumber): JsonElement {
                return if (keyOrNull == "age") JsonNumber(13) else jsonNumber
            }
        }, null)

        path.writeJson(transformed, overwrite = true)

        val expected = root.toPrettyString().replace("12", "13")
        val actual = path.readJson()?.toPrettyString()
        assertEquals(expected, actual)
    }

}