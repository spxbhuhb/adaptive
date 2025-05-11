package `fun`.adaptive.wireformat.json.visitor

import `fun`.adaptive.wireformat.json.elements.JsonArray
import `fun`.adaptive.wireformat.json.elements.JsonBoolean
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNull
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.elements.JsonObject
import `fun`.adaptive.wireformat.json.elements.JsonString

abstract class JsonVisitor<out R, in D> {

    abstract fun visitElement(element: JsonElement, data: D): R

    open fun visitArray(jsonArray: JsonArray, data: D): R =
        visitElement(jsonArray, data)

    open fun visitBoolean(jsonBoolean: JsonBoolean, data: D): R =
        visitElement(jsonBoolean, data)

    open fun visitNull(jsonNull: JsonNull, data: D): R =
        visitElement(jsonNull, data)

    open fun visitNumber(jsonNumber: JsonNumber, data: D): R =
        visitElement(jsonNumber, data)

    open fun visitObject(jsonObject: JsonObject, data: D): R =
        visitElement(jsonObject, data)

    open fun visitString(jsonString: JsonString, data: D): R =
        visitElement(jsonString, data)

}