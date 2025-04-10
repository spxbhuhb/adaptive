package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwGetValue(
    var name: String,
    var receiver: KwExpression? = null
) : KwElement, KwExpression {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {
        val safeReceiver = receiver

        if (safeReceiver == null) {
            writer.name(name)
        } else {
            writer
                .add(safeReceiver)
                .add(".")
                .name(name)
        }

        return writer
    }
}