package `fun`.adaptive.wireformat.signature

object CollectionsSignatures {

    const val ARRAY = "Lkotlin.Array<*>;"
    const val LIST = "Lkotlin.collections.List<*>;"
    const val SET = "Lkotlin.collections.Set<*>;"
    const val MAP = "Lkotlin.collections.Map<*,*>;"

    fun array(itemSignature : String): String =
        "Lkotlin.Array<$itemSignature>;"

    fun list(itemSignature : String): String =
        "Lkotlin.collections.List<$itemSignature>;"

    fun set(itemSignature : String): String =
        "Lkotlin.collections.List<$itemSignature>;"

    fun map(keySignature : String, valueSignature : String): String =
        "Lkotlin.collections.Map<$keySignature,$valueSignature>;"

}
