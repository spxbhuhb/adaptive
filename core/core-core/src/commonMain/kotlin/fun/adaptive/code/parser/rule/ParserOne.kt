package `fun`.adaptive.code.parser.rule

import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.code.parser.ParserEntry
import `fun`.adaptive.code.parser.ParserEntryDef

class ParserOne(
    val rules: List<ParserRule>
) : ParserRule() {

    override fun parse(def: ParserEntryDef?, parent : ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>? {

        for (rule in rules) {
            val children = rule.parse(null, parent, tokens) ?: continue
            return children
        }

        return null
    }

}