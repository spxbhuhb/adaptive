package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerManyTest {

    class TestRule(private val matchSize: Int) : LexerRule() {
        override fun match(source: CharArray, start: Int): Int {
            return if (start < source.size) matchSize else Lexer.NO_MATCH
        }
    }

    @Test
    fun `should match zero times when min is zero and no matches occur`() {
        val rule = TestRule(Lexer.NO_MATCH)
        val lexer = LexerMany(rule, min = 0)
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(0, result)
    }

    @Test
    fun `should fail to match when min is greater than available matches`() {
        val rule = TestRule(1)
        val lexer = LexerMany(rule, min = 5)
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should match exactly min times if available`() {
        val rule = TestRule(1)
        val lexer = LexerMany(rule, min = 3)
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(3, result)
    }

    @Test
    fun `should match up to max times`() {
        val rule = TestRule(1)
        val lexer = LexerMany(rule, max = 2)
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(2, result)
    }

    @Test
    fun `should stop matching when rule returns NO_MATCH`() {
        val rule = object : LexerRule() {
            var calls = 0
            override fun match(source: CharArray, start: Int): Int {
                calls++
                return if (calls <= 2) 1 else Lexer.NO_MATCH
            }
        }
        val lexer = LexerMany(rule)
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(2, result)
    }

    @Test
    fun `should stop matching when rule returns zero indicating a reverse match`() {
        val rule = object : LexerRule() {
            var calls = 0
            override fun match(source: CharArray, start: Int): Int {
                calls++
                return if (calls == 1) 1 else 0
            }
        }
        val lexer = LexerMany(rule)
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(1, result)
    }

    @Test
    fun `should handle empty source array`() {
        val rule = TestRule(1)
        val lexer = LexerMany(rule)
        val source = charArrayOf()
        val result = lexer.match(source, 0)
        assertEquals(0, result)
    }

}