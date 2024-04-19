package hu.simplexion.z2.serialization.json.elements

class JsonObject : JsonElement {
    val entries = mutableMapOf<String, JsonElement>()
}