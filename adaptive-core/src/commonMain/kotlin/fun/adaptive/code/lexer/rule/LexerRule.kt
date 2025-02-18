package `fun`.adaptive.code.lexer.rule

abstract class LexerRule {

    abstract fun match(source: CharArray, start: Int): Int

}