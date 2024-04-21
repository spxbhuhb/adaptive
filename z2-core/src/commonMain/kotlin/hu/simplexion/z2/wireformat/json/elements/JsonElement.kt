package hu.simplexion.z2.wireformat.json.elements

import hu.simplexion.z2.utility.UUID

interface JsonElement {

    val asBoolean: Boolean
        get() = throw IllegalStateException()

    val asInt: Int
        get() = throw IllegalStateException()

    val asFloat: Float
        get() = throw IllegalStateException()

    val asDouble: Double
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