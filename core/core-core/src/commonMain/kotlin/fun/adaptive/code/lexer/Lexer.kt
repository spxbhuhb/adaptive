package `fun`.adaptive.code.lexer

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Lexer {

    val tokenDefs = mutableListOf<LexerTokenDef>()

    fun tokenize(source: String) = tokenize(source.toCharArray())

    fun tokenize(source: CharArray): LexerTokenStream {
        val stream = LexerTokenStream()
        val defs = this.tokenDefs
        var offset = 0
        val end = source.size

        while (offset < end) {
            var bestMatch: LexerTokenDef? = null
            var bestMatchSize = 0

            for (def in defs) {
                val matchSize = def.rule.match(source, offset)

                if (matchSize == 0) continue
                if (matchSize <= bestMatchSize) continue

                bestMatch = def
                bestMatchSize = matchSize
            }

            if (bestMatch == null) {
                stream += LexerToken(offset, offset + 1, null, source[offset].toString())
                offset ++
            } else {
                if (bestMatch.skip != true) {
                    stream += LexerToken(
                        offset,
                        offset + bestMatchSize,
                        bestMatch,
                        source.concatToString(offset, offset + bestMatchSize)
                    )
                }
                offset += bestMatchSize
            }
        }

        return stream
    }

    class TokenProvider(
        val skip: Boolean,
        val channel: LexerChannel,
        val buildFun: LexerBuilder.() -> Unit
    ) {
        operator fun provideDelegate(thisRef: Lexer, prop: KProperty<*>): ReadOnlyProperty<Lexer, LexerTokenDef> {
            val def = LexerTokenDef(prop.name, LexerBuilder().apply(buildFun).rules.single(), skip, channel)
            thisRef.tokenDefs += def
            return def
        }
    }

    fun token(skip : Boolean = false, channel : LexerChannel = default, buildFun: LexerBuilder.() -> Unit) =
        TokenProvider(skip, channel, buildFun)

    fun fragment(buildFun: LexerBuilder.() -> Unit) =
        LexerBuilder().apply(buildFun).rules.single()

    companion object {
        const val NO_MATCH = - 1
        val default = LexerChannel("default")
        val hidden = LexerChannel("hidden")
    }

}