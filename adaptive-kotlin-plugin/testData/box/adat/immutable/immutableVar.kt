package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*

@Adat
class I1(
    val someInt: Int,
)

@Adat
class M3(
    var i1: I1
)

fun box(): String {

    if (M3.adatMetadata.isImmutable) return "Fail: M3.isImmutable"
    if (M3.adatMetadata["i1"].hasMutableValue) return "Fail: M3.i1.hasImmutableValue"

    return "OK"
}