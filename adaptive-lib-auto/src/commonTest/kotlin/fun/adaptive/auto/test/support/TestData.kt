package `fun`.adaptive.auto.test.support

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass

val td12 = TestData(12, "a")
val td23 = TestData(23, "b")
val td45 = TestData(45, "c")

val content_empty = listOf<TestData>()
val content_12 = listOf(td12)
val content_23 = listOf(td23)
val content_12_23 = listOf(td12, td23)
val content_45_23 = listOf(td45, td23)

@Adat
class TestData(
    val i: Int,
    val s: String
) : AdatClass