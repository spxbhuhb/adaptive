package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwConst(
    val kind: KwConstKind,
    val value: String
) : KwElement, KwExpression {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        when (kind) {
            KwConstKind.Boolean -> writer.add(value)
            KwConstKind.Number -> writer.add(value)
            KwConstKind.String ->
                writer
                    .openString()
                    .escapedString(value)
                    .closeString()
        }

}