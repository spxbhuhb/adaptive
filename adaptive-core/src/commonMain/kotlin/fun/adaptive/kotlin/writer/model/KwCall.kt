package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwCall(
    val symbol: KwSymbol
) : KwElement, KwExpression {

    val valueArguments = mutableListOf<KwValueArgument>()

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {

        if (valueArguments.isEmpty()) {
            writer
                .symbol(symbol)
                .noArgCall()

            return writer
        }

        val last = valueArguments.last()
        val lastValue = last.value

        if (last.name?.isEmpty() != false && lastValue is KwFunction && lastValue.isAnonymous) {
            writer.symbol(symbol)

            if (valueArguments.size > 1) {
                writer
                    .openParenthesis()
                    .join(valueArguments.dropLast(1))
                    .closeParenthesis()
            }

            writer.add(last.value)

            return writer
        }

        writer
            .symbol(symbol)
            .openParenthesis()
            .join(valueArguments)
            .closeParenthesis()

        return writer
    }

}