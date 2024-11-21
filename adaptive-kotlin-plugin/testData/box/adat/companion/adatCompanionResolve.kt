package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.*

@Adat
class TestAdat(
    var someInt: Int,
)

@AdatCompanionResolve
fun <A : AdatClass> someFun(
     companion : AdatCompanion<A>? = null
) : AdatCompanion<A> {
    return companion!!
}

@AdatCompanionResolve
fun <A : AdatClass> someOtherFun(
    data : A,
    companion : AdatCompanion<A>? = null
) : AdatCompanion<A> {
    return companion!!
}

fun box(): String {

    val c = someFun<TestAdat>()
    if (c.wireFormatName != "fun.adaptive.adat.TestAdat") "Fail: wrong name"

    val oc = someOtherFun(TestAdat(12))
    if (oc.wireFormatName != "fun.adaptive.adat.TestAdat") "Fail: wrong name"

    return "OK"
}