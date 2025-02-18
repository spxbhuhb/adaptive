package `fun`.adaptive.code.parser

import `fun`.adaptive.code.lexer.LexerBuilder
import `fun`.adaptive.code.lexer.LexerTokenStream
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Parser {

    // Entries may be recursive, or they may reference another declared later.
    // Therefore, we have to compile the actual rules only when all the entry
    // instances are available.

    val entryDefs = mutableListOf<ParserEntryDef>()
    val lock = getLock()
    var initialized = false

    abstract fun entryPoint(): ParserEntryDef

    fun parse(tokens: LexerTokenStream): ParserEntry? {
        initialize()
        return entryPoint().parse(tokens, parent = null)
    }

    fun initialize() {
        lock.use {
            if (! initialized) {
                entryDefs.forEach { it.initialize() }
                initialized = true
            }
        }
    }

    class EntryProvider(
        val skip: Boolean,
        val channel: ParserChannel,
        val buildFun: ParserBuilder.() -> Unit
    ) {
        operator fun provideDelegate(thisRef: Parser, prop: KProperty<*>): ReadOnlyProperty<Parser, ParserEntryDef> {
            val def = ParserEntryDef(prop.name, skip, channel, buildFun)
            thisRef.entryDefs += def
            return def
        }
    }

    fun entry(skip: Boolean = false, channel: ParserChannel = default, buildFun: ParserBuilder.() -> Unit) =
        EntryProvider(skip, channel, buildFun)

    fun fragment(buildFun: LexerBuilder.() -> Unit) =
        LexerBuilder().apply(buildFun).rules.single()

    companion object {
        const val NO_MATCH = - 1
        val default = ParserChannel("default")
        val hidden = ParserChannel("hidden")
    }

}