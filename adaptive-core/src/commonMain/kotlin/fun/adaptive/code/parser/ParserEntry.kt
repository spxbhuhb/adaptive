package `fun`.adaptive.code.parser

import `fun`.adaptive.code.lexer.LexerToken

data class ParserEntry(
    val start: LexerToken,
    val end: LexerToken,
    val def: ParserEntryDef? = null,
    val parent: ParserEntry? = null,
    val children: List<ParserEntry> = emptyList()
) {
    fun dump(indent: String = "  ", out : MutableList<String> = mutableListOf()) : MutableList<String> {
        out += "$indent${def?.name ?: start.def?.name ?: "ERROR"}  [${start.start}:${end.end}]"
        children.forEach { it.dump("$indent  ", out) }
        return out
    }
}