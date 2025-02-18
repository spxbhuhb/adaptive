package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerOneTest {

    class TestRule(private val matchSize: Int) : LexerRule() {
        override fun match(source: CharArray, start: Int): Int {
            return matchSize
        }
    }

    @Test
    fun `should return NO_MATCH when rules list is empty`() {
        val lexer = LexerOne(emptyList())
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should return the longest match from multiple rules`() {
        val rule1 = TestRule(2)
        val rule2 = TestRule(5)
        val rule3 = TestRule(3)

        val lexer = LexerOne(listOf(rule1, rule2, rule3))
        val source = "abcdef".toCharArray()
        val result = lexer.match(source, 0)

        assertEquals(5, result)
    }

    @Test
    fun `should return NO_MATCH when all rules return NO_MATCH`() {
        val rule1 = TestRule(Lexer.NO_MATCH)
        val rule2 = TestRule(Lexer.NO_MATCH)

        val lexer = LexerOne(listOf(rule1, rule2))
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)

        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should handle rules with varying match lengths`() {
        val rule1 = TestRule(4)
        val rule2 = TestRule(1)

        val lexer = LexerOne(listOf(rule1, rule2))
        val source = "abcde".toCharArray()
        val result = lexer.match(source, 0)

        assertEquals(4, result)
    }

    @Test
    fun `should return correct match when rules return mixed matches`() {
        val rule1 = TestRule(Lexer.NO_MATCH)
        val rule2 = TestRule(6)
        val rule3 = TestRule(3)

        val lexer = LexerOne(listOf(rule1, rule2, rule3))
        val source = "abcdefg".toCharArray()
        val result = lexer.match(source, 0)

        assertEquals(6, result)
    }

    @Test
    fun `should handle single rule`() {
        val rule = TestRule(5)

        val lexer = LexerOne(listOf(rule))
        val source = "abcde".toCharArray()
        val result = lexer.match(source, 0)

        assertEquals(5, result)
    }
}