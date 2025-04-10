package `fun`.adaptive.code.calc

import `fun`.adaptive.code.parser.Parser
import `fun`.adaptive.code.parser.ParserEntryDef

object CalcParser : Parser() {

    override fun entryPoint(): ParserEntryDef =
        expression

    val expression by entry {
        + CalcLexer.INT
        many {
            all {
                + operator
                + CalcLexer.INT
            }
        }
    }

    val operator by entry {
        one {
            + CalcLexer.PLUS
            + CalcLexer.MINUS
        }
    }

}