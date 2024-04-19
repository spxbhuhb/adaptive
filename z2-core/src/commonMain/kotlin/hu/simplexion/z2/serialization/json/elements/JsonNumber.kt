package hu.simplexion.z2.serialization.json.elements

class JsonNumber(val value: String) : JsonElement {

    override val asInt
        get() = value.toInt()

    override val asLong
        get() = value.toLong()

}