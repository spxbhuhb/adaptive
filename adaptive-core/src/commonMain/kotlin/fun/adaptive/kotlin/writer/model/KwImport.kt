package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwImport(
    val name : String,
    val alias : String? = null
) : KwElement {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        if (alias == null) {
            writer.add("import").add(name)
        } else {
            writer.add("import").add(name).add("as").add(alias)
        }

}