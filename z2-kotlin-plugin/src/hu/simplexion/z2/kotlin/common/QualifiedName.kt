package hu.simplexion.z2.kotlin.common

import org.jetbrains.kotlin.name.FqName

class QualifiedName(
    val asString : String
) {
    val asFqName = FqName.fromSegments(asString.split('.'))
}