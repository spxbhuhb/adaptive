package `fun`.adaptive.wireformat.json.visitor

import `fun`.adaptive.wireformat.json.elements.JsonArray
import `fun`.adaptive.wireformat.json.elements.JsonBoolean
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNull
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.elements.JsonObject
import `fun`.adaptive.wireformat.json.elements.JsonString

abstract class JsonTransformer<in D> : JsonVisitor<JsonElement, D>() {

    override fun visitElement(element: JsonElement, data: D) : JsonElement {
        element.transformChildren(this, data)
        return element
    }

    override fun visitArray(jsonArray: JsonArray, data: D): JsonElement =
        visitElement(jsonArray, data)

    override fun visitBoolean(jsonBoolean: JsonBoolean, data: D): JsonElement =
        visitElement(jsonBoolean, data)

    override fun visitNull(jsonNull: JsonNull, data: D): JsonElement =
        visitElement(jsonNull, data)

    override fun visitNumber(jsonNumber: JsonNumber, data: D): JsonElement =
        visitElement(jsonNumber, data)

    override fun visitObject(jsonObject: JsonObject, data: D): JsonElement =
        visitElement(jsonObject, data)

    override fun visitString(jsonString: JsonString, data: D): JsonElement =
        visitElement(jsonString, data)

}