package `fun`.adaptive.wireformat.json.example

import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.visitor.JsonTransformerVoidWithContext

class ExampleJsonTransformer : JsonTransformerVoidWithContext() {
    override fun visitNumber(jsonNumber: JsonNumber): JsonElement {
        return if (keyOrNull == "age") JsonNumber(13) else jsonNumber
    }
}