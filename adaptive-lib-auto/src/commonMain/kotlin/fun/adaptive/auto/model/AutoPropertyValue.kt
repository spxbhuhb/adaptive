package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat

@Adat
class AutoPropertyValue(
    val changeTime : LamportTimestamp,
    val propertyName: String,
    val payload: ByteArray
)