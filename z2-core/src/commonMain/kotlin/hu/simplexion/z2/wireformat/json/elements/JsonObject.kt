package hu.simplexion.z2.wireformat.json.elements

class JsonObject : JsonElement {
    val entries = mutableMapOf<String, JsonElement>()
}