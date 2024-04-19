package hu.simplexion.z2.kotlin.util

import org.jetbrains.kotlin.name.Name

class LocalName(
    val asString : String
) {
    val asName = Name.identifier(asString)

}