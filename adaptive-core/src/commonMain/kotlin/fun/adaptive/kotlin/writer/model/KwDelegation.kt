package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwDelegation(
    property : KwProperty
) : KwElement, KwExpression {

    var delegate = KwFunction("<delegate-$property>")

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        delegate.toKotlin(writer)

}