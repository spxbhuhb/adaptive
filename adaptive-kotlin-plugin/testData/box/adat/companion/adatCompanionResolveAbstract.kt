package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.*

abstract class AbstractTestAdat : AdatClass {
    abstract var someInt: Int
}

@Adat
class TestAdat(
    override var someInt: Int
) : AbstractTestAdat()

@AdatCompanionResolve
fun <A : AdatClass> someFun(
     companion : AdatCompanion<A>? = null
) : AdatCompanion<A>? {
    return companion
}

@AdatCompanionResolve
fun <A : AdatClass> someOtherFun(
    data : A,
    companion : AdatCompanion<A>? = null
) : AdatCompanion<A>? {
    return companion
}

fun box(): String {

    val c = someFun<AbstractTestAdat>()
    if (c != null) return "Fail: companion is not null for abstract class"

    val c2 = someFun<TestAdat>()
    if (c2 == null) return "Fail: 'c2' is null with a non-abstract class"
    if (c2.wireFormatName != "fun.adaptive.adat.TestAdat") return "Fail: wrong name"

    val oc = someOtherFun(TestAdat(12))
    if (oc == null) return "Fail: 'oc' is null with a non-abstract class"
    if (oc.wireFormatName != "fun.adaptive.adat.TestAdat") return "Fail: wrong name"

    return "OK"
}