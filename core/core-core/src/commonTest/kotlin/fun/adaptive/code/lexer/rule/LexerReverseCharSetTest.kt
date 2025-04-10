package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.lexer.Lexer
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerReverseCharSetTest {

    @Test
    fun `should match full string when no chars from set are found`() {
        val lexer = LexerReverseCharSet(charArrayOf('x', 'y', 'z'))
        val source = "abcdef".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(source.size, result)
    }

    @Test
    fun `should match up to first occurrence of char in set`() {
        val lexer = LexerReverseCharSet(charArrayOf('c'))
        val source = "abcde".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(2, result) // stops before 'c'
    }

    @Test
    fun `should return zero if first character is in the set`() {
        val lexer = LexerReverseCharSet(charArrayOf('a'))
        val source = "abc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(0, result)
    }

    @Test
    fun `should match correctly when start is beyond array size`() {
        val lexer = LexerReverseCharSet(charArrayOf('a'))
        val source = "abc".toCharArray()
        val result = lexer.match(source, source.size)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should match correctly with empty source`() {
        val lexer = LexerReverseCharSet(charArrayOf('a'))
        val source = charArrayOf()
        val result = lexer.match(source, 0)
        assertEquals(Lexer.NO_MATCH, result)
    }

    @Test
    fun `should match full string if set is empty`() {
        val lexer = LexerReverseCharSet(charArrayOf())
        val source = "abcde".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(source.size, result)
    }

    @Test
    fun `should match substring from a non-zero start`() {
        val lexer = LexerReverseCharSet(charArrayOf('e'))
        val source = "abcde".toCharArray()
        val result = lexer.match(source, 2)
        assertEquals(2, result) // matches "cd" before "e"
    }

    @Test
    fun `should handle multiple occurrences of set characters`() {
        val lexer = LexerReverseCharSet(charArrayOf('a', 'b'))
        val source = "aabbcc".toCharArray()
        val result = lexer.match(source, 2)
        assertEquals(0, result) // 'b' found immediately
    }

    @Test
    fun `should handle set with multiple characters`() {
        val lexer = LexerReverseCharSet(charArrayOf('x', 'y', 'z'))
        val source = "abcxyz".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(3, result) // stops at 'x'
    }

}