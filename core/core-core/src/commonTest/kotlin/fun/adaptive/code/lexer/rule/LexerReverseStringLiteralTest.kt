package `fun`.adaptive.code.lexer.rule

import `fun`.adaptive.code.g4.G4Lexer
import `fun`.adaptive.code.lexer.LexerToken
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexerReverseStringLiteralTest {

    @Test
    fun `should throw exception for empty char array`() {
        assertFailsWith<IllegalStateException> {
            LexerReverseStringLiteral(charArrayOf())
        }
    }

    @Test
    fun `should match full string when no matching characters`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('x'))
        val source = "abcdefg".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(source.size, result)
    }

    @Test
    fun `should match substring when matching characters found`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('c', 'd'))
        val source = "abcde".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(2, result) // "ab" before "cd"
    }

    @Test
    fun `should handle multiple occurrences of the target sequence`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('a', 'b'))
        val source = "xabababc".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(1, result) // Only "x" is matched before the first "ab"
    }

    @Test
    fun `should return correct length when starting from non-zero position`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('b', 'c'))
        val source = "aabbcc".toCharArray()
        val result = lexer.match(source, 2)
        assertEquals(1, result) // "b" matched before "bc"
    }

    @Test
    fun `should match full string if start beyond length`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('x'))
        val source = "abc".toCharArray()
        val result = lexer.match(source, 5)
        assertEquals(- 1, result)
    }

    @Test
    fun `should match correctly with single character`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('d'))
        val source = "abcd".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(3, result) // "abc" matched before "d"
    }

    @Test
    fun `should match correctly when characters appear at the end`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('e', 'f'))
        val source = "abcdeff".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(4, result) // "abcd" matched before "ef"
    }

    @Test
    fun `should handle matching pattern at the start`() {
        val lexer = LexerReverseStringLiteral(charArrayOf('a', 'b'))
        val source = "abcdef".toCharArray()
        val result = lexer.match(source, 0)
        assertEquals(0, result) // pattern found immediately
    }

}