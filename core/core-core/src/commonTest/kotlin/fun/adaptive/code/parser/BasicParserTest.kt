package `fun`.adaptive.code.parser

import `fun`.adaptive.code.calc.CalcLexer
import `fun`.adaptive.code.calc.CalcParser
import kotlin.test.Test

class BasicParserTest {

    @Test
    fun basic() {
        val lexer = CalcLexer
        val parser = CalcParser

        val source = "1 + 2"

        val tokenStream = lexer.tokenize(source).nonHidden()
        val ast = parser.parse(tokenStream)
        println(ast?.dump()?.joinToString("\n"))

    }

}