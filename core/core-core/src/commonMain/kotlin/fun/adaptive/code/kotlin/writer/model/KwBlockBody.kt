package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

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