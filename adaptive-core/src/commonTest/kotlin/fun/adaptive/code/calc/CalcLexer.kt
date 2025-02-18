package `fun`.adaptive.code.calc

import `fun`.adaptive.code.lexer.Lexer

object CalcLexer : Lexer() {

    val INT by token {
        many { charRange { '0' .. '9' } }
    }

    val PLUS by token { char { '+' } }

    val MINUS by token { char { '-' } }

    val WSNLCHARS = fragment { charSet { " \t\n\r\u000C" } }

    val WS by token(channel = hidden) {
        many(min = 1) {
            + WSNLCHARS
        }
    }

}