package `fun`.adaptive.wireformat.json.visitor

import `fun`.adaptive.wireformat.json.elements.JsonArray
import `fun`.adaptive.wireformat.json.elements.JsonBoolean
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNull
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.elements.JsonObject
import `fun`.adaptive.wireformat.json.elements.JsonString

abstract class JsonTransformerVoid : JsonTransformer<Nothing?>() {

    open fun visitElement(element: JsonElement) : JsonElement =
        visitElement(element, null)

    final override fun visitElement(element: JsonElement, data: Nothing?) : JsonElement {
        element.transformChildren(this, data)
        return element
    }

    open fun visitArray(jsonArray: JsonArray) : JsonElement =
        visitElement(jsonArray, null)

    final override fun visitArray(jsonArray: JsonArray, data: Nothing?): JsonElement =
        visitArray(jsonArray)

    open fun visitBoolean(jsonBoolean: JsonBoolean) : JsonElement =
        visitElement(jsonBoolean, null)

    final override fun visitBoolean(jsonBoolean: JsonBoolean, data: Nothing?): JsonElement =
        visitBoolean(jsonBoolean)

    open fun visitNull(jsonNull: JsonNull) : JsonElement =
        visitElement(jsonNull, null)

    final override fun visitNull(jsonNull: JsonNull, data: Nothing?): JsonElement =
        visitNull(jsonNull)

    open fun visitNumber(jsonNumber: JsonNumber) : JsonElement =
        visitElement(jsonNumber, null)

    final override fun visitNumber(jsonNumber: JsonNumber, data: Nothing?): JsonElement =
        visitNumber(jsonNumber)

    open fun visitObject(jsonObject: JsonObject) : JsonElement =
        visitElement(jsonObject, null)

    final override fun visitObject(jsonObject: JsonObject, data: Nothing?): JsonElement =
        visitObject(jsonObject)

    open fun visitString(jsonString: JsonString) : JsonElement =
        visitElement(jsonString, null)

    final override fun visitString(jsonString: JsonString, data: Nothing?): JsonElement =
        visitString(jsonString)

}