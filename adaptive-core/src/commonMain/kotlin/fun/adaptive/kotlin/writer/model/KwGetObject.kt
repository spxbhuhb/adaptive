package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwGetObject(
    var symbol: KwSymbol
) : KwElement, KwExpression {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        writer.symbol(symbol)

}