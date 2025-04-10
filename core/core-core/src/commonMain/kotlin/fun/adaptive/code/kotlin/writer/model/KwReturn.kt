package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwReturn(
    var value: KwExpression
) : KwStatement {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        writer.add("return ").add(value)

}