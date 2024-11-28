package `fun`.adaptive.utility

import kotlin.test.Test
import kotlin.test.assertEquals

class StringTest {

    @Test
    fun encode() {
        val testCases = listOf(
            "" to "",                                   // Empty string
            "hello" to "hello",                        // Simple text
            "Hello World!" to "Hello+World%21",        // Space and special character
            "a+b=c&d=e" to "a%2Bb%3Dc%26d%3De",        // Reserved characters
            "école" to "%C3%A9cole",                   // UTF-8 accented character
            "中文测试" to "%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95", // Non-ASCII characters
            "100% free" to "100%25+free",              // Percent sign
            "💖 Love" to "%F0%9F%92%96+Love",          // Emoji
            "árvíztűrő tükörfúrógép" to "%C3%A1rv%C3%ADzt%C5%B1r%C5%91+t%C3%BCk%C3%B6rf%C3%BAr%C3%B3g%C3%A9p",
            "ÁRVÍZTŰRŐ TÜKÖRFÚRÓGÉP" to "%C3%81RV%C3%8DZT%C5%B0R%C5%90+T%C3%9CK%C3%96RF%C3%9AR%C3%93G%C3%89P"
        )

        for ((text, encodedText) in testCases) {
            assertEquals(encodedText, text.encodeToUrl())
        }
    }

    @Test
    fun decode() {
        val testCases = listOf(
            "" to "",                                   // Empty string
            "hello" to "hello",                        // Simple text
            "Hello World!" to "Hello+World%21",        // Space and special character
            "a+b=c&d=e" to "a%2Bb%3Dc%26d%3De",        // Reserved characters
            "école" to "%C3%A9cole",                   // UTF-8 accented character
            "中文测试" to "%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95", // Non-ASCII characters
            "100% free" to "100%25+free",              // Percent sign
            "💖 Love" to "%F0%9F%92%96+Love",          // Emoji
            "árvíztűrő tükörfúrógép" to "%C3%A1rv%C3%ADzt%C5%B1r%C5%91+t%C3%BCk%C3%B6rf%C3%BAr%C3%B3g%C3%A9p",
            "ÁRVÍZTŰRŐ TÜKÖRFÚRÓGÉP" to "%C3%81RV%C3%8DZT%C5%B0R%C5%90+T%C3%9CK%C3%96RF%C3%9AR%C3%93G%C3%89P"
        )

        for ((text, encodedText) in testCases) {
            assertEquals(text, encodedText.decodeFromUrl())
        }
    }
}