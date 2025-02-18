package `fun`.adaptive.code.parser.rule

import `fun`.adaptive.code.lexer.LexerTokenDef
import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.code.parser.ParserEntry
import `fun`.adaptive.code.parser.ParserEntryDef

class ParserSingleToken(
    val token: LexerTokenDef
) : ParserRule() {

    override fun parse(def: ParserEntryDef?, parent: ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>? {
        if (tokens.peek() != token) null

        val token = tokens.next()

        return listOf(
            ParserEntry(
                start = token,
                end = token,
                def = def,
                parent = parent
            )
        )
    }

}