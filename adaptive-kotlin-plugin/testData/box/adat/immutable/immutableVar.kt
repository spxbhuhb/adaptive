package `fun`.adaptive.adat

@Adat
class I1(
    val someInt: Int,
)

@Adat
class M3(
    var i1: I1
)

fun box(): String {

    if (M3.adatMetadata.isImmutableClass) return "Fail: M3.isImmutableClass"
    if (M3.adatMetadata["i1"].hasMutableValue) return "Fail: M3.i1.hasImmutableValue"

    return "OK"
}