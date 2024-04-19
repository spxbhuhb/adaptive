package hu.simplexion.z2.serialization.json.elements

import hu.simplexion.z2.util.UUID

class JsonString(val value: String) : JsonElement {

    override val asString
        get() = value

    @OptIn(ExperimentalStdlibApi::class)
    override val asByteArray
        get() = value.hexToByteArray()

    override fun <T> asUuid(): UUID<T> {
        return UUID(value)
    }

}