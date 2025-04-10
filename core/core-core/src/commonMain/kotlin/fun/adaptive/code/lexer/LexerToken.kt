package `fun`.adaptive.code.lexer

data class LexerToken(
    val start: Int,
    val end: Int,
    val def: LexerTokenDef? = null,
    val value: String? = null
) {
    override fun toString(): String {
        return "LexerToken(start=$start, end=$end, def=${def?.name ?: "ERROR"}, value=$value)"
    }
}