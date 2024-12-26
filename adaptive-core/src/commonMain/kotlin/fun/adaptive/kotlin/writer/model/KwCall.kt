package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwCall(
    val symbol: KwSymbol
) : KwElement, KwExpression, KwExpressionScope {

    var openFormat = false
    var dispatchReceiver : KwExpression? = null
    val valueArguments = mutableListOf<KwValueArgument>()

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {

        dispatchReceiver?.let {
            writer.add(it)
            writer.add(".")
        }

        if (valueArguments.isEmpty()) {
            writer
                .symbol(symbol)
                .noArgCall()

            return writer
        }

        val last = valueArguments.last()
        val lastValue = last.value

        writer.symbol(symbol)

        if (last.name?.isEmpty() != false && lastValue is KwFunction && lastValue.isAnonymous) {
            if (valueArguments.size > 1) {
                addArguments(writer, valueArguments.subList(0, valueArguments.size - 1))
            }
            writer.add(last.value)
        } else {
            addArguments(writer, valueArguments)
        }

        return writer
    }

    private fun addArguments(writer : KotlinWriter, arguments: List<KwValueArgument>) {
        if (openFormat) {
            writer
                .openParenthesis()
                .newLine()
                .withIndent {
                    join(arguments, multiLine = true)
                }
                .newLine()
                .closeParenthesis()
        } else {
            writer
                .openParenthesis()
                .join(arguments)
                .closeParenthesis()
        }
    }

}