package `fun`.adaptive.wireformat.json.visitor

import `fun`.adaptive.wireformat.json.elements.JsonArray
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonObject

abstract class JsonTransformerVoidWithContext : JsonTransformerVoid() {

    class StackEntry(val container: JsonElement, val key: String?, val index: Int?)

    val containerStack = mutableListOf<StackEntry>()

    val containerOrNull get() = containerStack.lastOrNull()?.container
    val keyOrNull get() = containerStack.lastOrNull()?.key
    val indexOrNull get() = containerStack.lastOrNull()?.index

    final override fun visitArray(jsonArray: JsonArray): JsonElement {
        val items = jsonArray.value
        for (index in items.indices) {
            containerStack.add(StackEntry(jsonArray, null, index))
            items[index] = items[index].transform(this, null)
            containerStack.removeLast()
        }
        return jsonArray
    }

    open fun visitArrayNew(jsonArray: JsonArray): JsonElement {
        return super.visitArray(jsonArray)
    }

    final override fun visitObject(jsonObject: JsonObject): JsonElement {
        for (entry in jsonObject.value) {
            containerStack.add(StackEntry(jsonObject, entry.key, null))
            entry.setValue(entry.value.transform(this, null))
            containerStack.removeLast()
        }
        return jsonObject
    }

    open fun visitObjectNew(jsonObject: JsonObject): JsonElement {
        return super.visitObject(jsonObject)
    }
}