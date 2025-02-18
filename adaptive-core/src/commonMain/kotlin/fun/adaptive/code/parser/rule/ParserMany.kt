package `fun`.adaptive.code.parser.rule

import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.code.parser.ParserEntry
import `fun`.adaptive.code.parser.ParserEntryDef

class ParserMany(
    val rule: ParserRule,
    val min: Int = 0,
    val max: Int = Int.MAX_VALUE
) : ParserRule() {

    override fun parse(def: ParserEntryDef?, parent: ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>? {

        val result = mutableListOf<ParserEntry>()

        var count = 0

        while (count < max) {
            if (! tokens.hasNext()) break
            val children = rule.parse(null, parent, tokens) ?: break
            result.addAll(children)
            count ++
        }

        return if (count >= min) result else null
    }
}