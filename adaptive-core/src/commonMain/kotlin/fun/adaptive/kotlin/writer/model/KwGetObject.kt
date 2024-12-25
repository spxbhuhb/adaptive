package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwGetObject(
    var name: String
) : KwElement, KwExpression {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        writer.add(name)

}