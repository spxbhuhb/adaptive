package `fun`.adaptive.code.lexer

class LexerTokenStream(
    private val tokens : MutableList<LexerToken> = mutableListOf<LexerToken>()
) {

    var position = 0

    operator fun plusAssign(token: LexerToken) {
        tokens += token
    }

    operator fun get(index: Int): LexerToken = tokens[index]

    fun peek() : LexerToken? =
        if (position < tokens.size) tokens[position] else null

    fun lookBack() : LexerToken =
        tokens[position - 1]

    fun hasNext() : Boolean = position < tokens.size

    fun next() : LexerToken =
        tokens[position++]

    fun toList(): List<LexerToken> = tokens.toList()

    fun nonHidden() = LexerTokenStream(tokens.filter { it.def?.channel != Lexer.hidden }.toMutableList())

}