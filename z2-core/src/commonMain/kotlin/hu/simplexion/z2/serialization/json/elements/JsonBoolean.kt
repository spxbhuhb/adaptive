package hu.simplexion.z2.serialization.json.elements

class JsonBoolean(
    val value: Boolean
) : JsonElement {

    override val asBoolean: Boolean
        get() = value
}