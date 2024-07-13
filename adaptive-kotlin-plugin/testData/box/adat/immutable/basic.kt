package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.service.*
import hu.simplexion.adaptive.service.transport.*

@Adat
class I1(
    val someInt: Int,
    val someBoolean: Boolean,
    val someIntListSet: Set<List<Int>>
)

@Adat
class M1(
    var someInt: Int,
    val someBoolean: Boolean,
    val someIntListSet: Set<List<Int>>
)

@Adat
class M2(
    val someInt: Int,
    val someBoolean: Boolean,
    val someIntListSet: MutableSet<List<Int>>
)

fun box(): String {

    if (! I1.adatMetadata.isImmutable) return "Fail: I1.isImmutable == false"

    if (M1.adatMetadata.isImmutable) return "Fail: M1.isImmutable == true"
    if (M2.adatMetadata.isImmutable) return "Fail: M2.isImmutable == true"

    return "OK"
}