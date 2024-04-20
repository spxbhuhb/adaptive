package hu.simplexion.z2.wireformat.json.elements

class JsonArray : JsonElement {

    val items = mutableListOf<JsonElement>()

    override val asIntList: MutableList<Int>
        get() = items.map { it.asInt }.toMutableList()

}