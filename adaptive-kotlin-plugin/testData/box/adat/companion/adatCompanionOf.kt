package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.*

@Adat
class TestAdat(
    var someInt: Int,
)

@Adat
class TestAdatWithCompanion(
    var someInt: Int,
) {
    companion object {
        val a = "A"
    }
}

fun box(): String {

    val t1 = adatCompanionOf<TestAdat>()

    if (t1.wireFormatName != "fun.adaptive.adat.TestAdat") "Fail: wrong name"


    val t2 = adatCompanionOf<TestAdatWithCompanion>()

    if (t2.wireFormatName != "fun.adaptive.adat.TestAdatWithCompanion") "Fail: wrong name != TestAdatWithCompanion"

    return "OK"
}