package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwExpressionBody : KwBody(), KwExpression {

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {
        check(statements.size == 1) { "expression body with zero or multiple statements" }
        statements.first().toKotlin(writer)
        return writer
    }

}