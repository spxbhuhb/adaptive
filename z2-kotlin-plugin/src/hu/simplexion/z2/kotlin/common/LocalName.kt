package hu.simplexion.z2.kotlin.common

import org.jetbrains.kotlin.name.Name

class LocalName(
    val asString : String
) {
    val asName = Name.identifier(asString)

}