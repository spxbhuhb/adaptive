package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwGetObject(
    var symbol: KwSymbol
) : KwElement, KwExpression {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        writer.symbol(symbol)

}