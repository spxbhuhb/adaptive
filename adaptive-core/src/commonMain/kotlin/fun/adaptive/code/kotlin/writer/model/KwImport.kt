package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwImport(
    val symbol : KwSymbol,
    val alias : String? = null
) : KwElement {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        if (alias == null) {
            writer.add("import").add(symbol.name)
        } else {
            writer.add("import").add(symbol.name).add("as").add(alias)
        }

}