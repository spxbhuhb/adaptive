package hu.simplexion.z2.serialization.json.elements

import hu.simplexion.z2.util.UUID

interface JsonElement {

    val asBoolean: Boolean
        get() = throw IllegalStateException()

    val asInt: Int
        get() = throw IllegalStateException()

    val asIntList: MutableList<Int>
        get() = throw IllegalStateException()

    val asLong: Long
        get() = throw IllegalStateException()

    val asString: String
        get() = throw IllegalStateException()

    val asByteArray: ByteArray
        get() = throw IllegalStateException()

    fun <T> asUuid(): UUID<T> {
        throw IllegalStateException()
    }
}