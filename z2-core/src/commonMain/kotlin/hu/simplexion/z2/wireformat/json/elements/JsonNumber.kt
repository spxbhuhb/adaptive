package hu.simplexion.z2.wireformat.json.elements

class JsonNumber(val value: String) : JsonElement {

    override val asInt
        get() = value.toInt()

    override val asLong
        get() = value.toLong()

    override val asFloat
        get() = value.toFloat()

    override val asDouble
        get() = value.toDouble()

}