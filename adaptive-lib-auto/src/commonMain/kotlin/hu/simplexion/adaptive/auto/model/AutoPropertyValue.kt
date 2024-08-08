package hu.simplexion.adaptive.auto.model

import hu.simplexion.adaptive.adat.Adat

@Adat
class AutoPropertyValue(
    val propertyName: String,
    val payload: ByteArray
)