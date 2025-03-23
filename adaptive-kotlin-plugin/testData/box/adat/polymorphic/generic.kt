package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.toJson
import `fun`.adaptive.adat.AdatCompanion

@Adat
class T1<T>(
    var something: T?
)

fun box(): String {
    val tl = T1<Int>(12)

    val json = tl.toJson((tl.adatCompanion as AdatCompanion<T1<Int>>).adatWireFormat)

    val tlr = (tl.adatCompanion as AdatCompanion<T1<Int>>).fromJson(json)

    if (tl != tlr) return "Fail: ${tlr}"

    return "OK"
}