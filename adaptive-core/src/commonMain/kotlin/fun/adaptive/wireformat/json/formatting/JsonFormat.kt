package `fun`.adaptive.wireformat.json.formatting

import `fun`.adaptive.wireformat.json.elements.JsonArray
import `fun`.adaptive.wireformat.json.elements.JsonBoolean
import `fun`.adaptive.wireformat.json.elements.JsonElement
import `fun`.adaptive.wireformat.json.elements.JsonNull
import `fun`.adaptive.wireformat.json.elements.JsonNumber
import `fun`.adaptive.wireformat.json.elements.JsonObject
import `fun`.adaptive.wireformat.json.elements.JsonString

class JsonFormat(
    val indentIncrement: String = "  "
) {

    fun format(element: JsonElement): String {
        val builder = StringBuilder()
        prettyString(builder, element, "")
        return builder.toString()
    }

    fun prettyString(builder: StringBuilder, element: JsonElement, indent: String) {
        when (element) {

            is JsonNull -> builder.append("null")

            is JsonNumber -> builder.append(element.toString())

            is JsonString -> builder.append(element.toString())

            is JsonBoolean -> builder.append(element.toString())

            is JsonArray -> {
                if (element.items.isEmpty()) {
                    builder.append("[]")
                    return
                }

                builder.append("[\n")

                val itemIndent = indent + indentIncrement

                for ((index, item) in element.items.withIndex()) {

                    builder.append(itemIndent)
                    prettyString(builder, item, itemIndent)

                    if (index < element.items.size - 1) {
                        builder.append(',')
                    }

                    builder.append('\n')
                }

                builder.append(indent).append("]")
            }

            is JsonObject -> {
                val entries = element.entries.toList()

                if (entries.isEmpty()) {
                    builder.append("{}")
                    return
                }

                builder.append("{\n")

                val itemIndent = indent + indentIncrement

                for ((index, entry) in entries.withIndex()) {
                    val (key, value) = entry

                    builder.append("$itemIndent\"$key\": ")

                    prettyString(builder, value, itemIndent)

                    if (index < entries.size - 1) {
                        builder.append(',')
                    }

                    builder.append('\n')
                }

                builder.append(indent).append("}")
            }
        }
    }

}