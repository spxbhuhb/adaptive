package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwValueArgument(
    var name : String?,
    var value : KwExpression
) : KwElement {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {
        val safeName = name

        if (safeName != null) {
            writer.add(safeName).add("=").add(value)
        } else {
            writer.add(value)
        }

        return writer
    }

}