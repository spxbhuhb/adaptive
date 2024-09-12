package ize

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.wireformat.toJson
import kotlin.io.println
import kotlin.text.decodeToString

@Suppress("unused")
@Adat
private class TA(
    val e: TE,
    val eSet: Set<TE>
)

private enum class TE {
    V1,
    V2
}

fun box(): String {

    // {
    //  "version": 1,
    //  "name": "ize.TA",
    //  "flags": 1,
    //  "properties": [
    //    {
    //      "name": "e",
    //      "index": 0,
    //      "flags": 3,
    //      "signature": "Lize.TE;",
    //      "properties": []
    //    },
    //    {
    //      "name": "eSet",
    //      "index": 1,
    //      "flags": 3,
    //      "signature": "Lkotlin.collections.Set<*>;",
    //      "properties": []
    //    }
    //  ]
    // }

    // println(TA.adatMetadata.toJson(AdatClassMetadata).decodeToString())

    if (TA.adatMetadata.properties[1].signature != "Lkotlin.collections.Set<Lize.TE;>;") return "Fail: ${TA.adatMetadata.properties[1].signature}"
    return "OK"
}