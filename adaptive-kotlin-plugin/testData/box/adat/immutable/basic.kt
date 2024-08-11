package `fun`.adaptive.adat

import `fun`.adaptive.service.*
import `fun`.adaptive.service.transport.*

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

    if (I1.adatMetadata.isMutable) return "Fail: I1.isMutable"

    if (M1.adatMetadata.isImmutable) return "Fail: M1.isImmutable"
    if (M2.adatMetadata.isImmutable) return "Fail: M2.isImmutable"

    return "OK"
}