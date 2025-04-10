package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerCharSetTest {


    @Test
    fun `should match single character in set`() {
        val lexer = LexerCharSet(charArrayOf('a', 'b', 'c'))
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(1, result)
    }

    @Test
    fun `should not match character not in set`() {
        val lexer = LexerCharSet(charArrayOf('x', 'y', 'z'))
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should match character at different positions`() {
        val lexer = LexerCharSet(charArrayOf('a', 'b', 'c'))
        val source = "abc".toCharArray()

        val result1 = lexer.match(source, 0)
        val result2 = lexer.match(source, 1)
        val result3 = lexer.match(source, 2)

        assertEquals(1, result1)
        assertEquals(1, result2)
        assertEquals(1, result3)
    }

    @Test
    fun `should return NO_MATCH when starting beyond array length`() {
        val lexer = LexerCharSet(charArrayOf('a'))
        val source = "a".toCharArray()
        val result = lexer.match(source, 5)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should match with single character set`() {
        val lexer = LexerCharSet(charArrayOf('z'))
        val source = "z".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(1, result)
    }

    @Test
    fun `should handle empty source array`() {
        val lexer = LexerCharSet(charArrayOf('a'))
        val source = charArrayOf()
        val result = lexer.match(source, 0)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should handle empty char set`() {
        val lexer = LexerCharSet(charArrayOf())
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(Lexer.NO_MATCH, result)
    }
}