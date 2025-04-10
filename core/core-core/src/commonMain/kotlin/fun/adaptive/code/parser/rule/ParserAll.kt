package `fun`.adaptive.code.parser.rule

import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.code.parser.ParserEntry
import `fun`.adaptive.code.parser.ParserEntryDef

class ParserAll(
    val rules: List<ParserRule>
) : ParserRule() {

    override fun parse(def: ParserEntryDef?, parent : ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>? {
        val result = mutableListOf<ParserEntry>()

        for (rule in rules) {
            val children = rule.parse(null, parent, tokens) ?: return null
            result.addAll(children)
        }

        return result
    }

}