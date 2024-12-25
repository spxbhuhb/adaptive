package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwBlockBody : KwBody() {

    override fun toKotlin(writer: KotlinWriter) =
        writer
            .openBlock()
            .newLine()
            .withIndent {
                statements.forEach {
                    it.toKotlin(writer)
                    writer.newLine()
                }
            }
            .closeBlock()
            .newLine()

}