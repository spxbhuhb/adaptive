package `fun`.adaptive.code.parser

import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.code.parser.rule.ParserAll
import `fun`.adaptive.code.parser.rule.ParserRule
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ParserEntryDef(
    var name: String,
    val skip: Boolean,
    val channel: ParserChannel,
    val buildFun: ParserBuilder.() -> Unit
) : ReadOnlyProperty<Parser, ParserEntryDef>, ParserRule() {

    // this placeholder is replaced in the init block of Parser
    var rule: ParserRule = PLACEHOLDER

    override fun getValue(thisRef: Parser, property: KProperty<*>): ParserEntryDef {
        return this
    }

    fun initialize() {
        val rules = ParserBuilder().apply(buildFun).rules
        if (rules.size == 1) {
            rule = rules[0]
        } else {
            rule = ParserAll(rules)
        }
    }

    fun parse(tokens: LexerTokenStream, parent: ParserEntry?): ParserEntry? {
        val start = tokens.peek() ?: return null
        val children = rule.parse(this, parent, tokens) ?: return null
        val end = tokens.lookBack()

        return ParserEntry(
            start,
            end,
            this,
            parent,
            children
        )
    }

    override fun parse(def: ParserEntryDef?, parent: ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>? {
        val entry = parse(tokens, parent) ?: return null
        return listOf(entry)
    }

    companion object {
        private val PLACEHOLDER = object : ParserRule() {
            override fun parse(def: ParserEntryDef?, parent: ParserEntry?, tokens: LexerTokenStream): List<ParserEntry>? {
                return null
            }
        }
    }

}