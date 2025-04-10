package `fun`.adaptive.code.parser.rule

import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.code.parser.ParserEntry
import `fun`.adaptive.code.parser.ParserEntryDef

abstract class ParserRule {

    abstract fun parse(def: ParserEntryDef?, parent : ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>?

}