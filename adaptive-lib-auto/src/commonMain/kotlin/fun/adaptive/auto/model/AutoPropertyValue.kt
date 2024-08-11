package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat

@Adat
class AutoPropertyValue(
    val propertyName: String,
    val payload: ByteArray
)