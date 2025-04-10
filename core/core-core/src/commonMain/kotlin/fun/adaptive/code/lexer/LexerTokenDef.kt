package `fun`.adaptive.code.lexer

import `fun`.adaptive.code.lexer.rule.LexerRule
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LexerTokenDef(
    var name : String,
    val rule: LexerRule,
    val skip: Boolean,
    val channel: LexerChannel
) : ReadOnlyProperty<Lexer, LexerTokenDef> {

    override fun getValue(thisRef: Lexer, property: KProperty<*>): LexerTokenDef {
        return this
    }

}