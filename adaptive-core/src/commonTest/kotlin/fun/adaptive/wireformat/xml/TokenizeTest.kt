/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.xml

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TokenizeTest {

    @Test
    fun basic() {
        val tokens = tokenize(basic)

        var index = 0

        fun List<Token>.sTag(content: String): List<Token> {
            assertEquals(Token(TokenType.STag, content), this[index ++])
            return this
        }

        fun List<Token>.eTag(content: String): List<Token> {
            assertEquals(Token(TokenType.ETag, content), this[index ++])
            return this
        }

        fun List<Token>.emptyElemTag(content: String): List<Token> {
            assertEquals(Token(TokenType.EmptyElemTag, content), this[index ++])
            return this
        }

        fun List<Token>.content(content: String): List<Token> {
            assertEquals(Token(TokenType.Content, content), this[index ++])
            return this
        }

        fun List<Token>.spaces(): List<Token> {
            assertTrue(this[index].value.isBlank())
            assertEquals(TokenType.Content, this[index ++].type)
            return this
        }

        with(tokens) {
            sTag("people").spaces()

            sTag("person id=\"1\"").spaces()
            sTag("name")
            content("John Doe")
            eTag("name").spaces()
            sTag("age")
            content("30")
            eTag("age").spaces()
            eTag("person").spaces()

            sTag("person id=\"2\"").spaces()
            sTag("name")
            content("Jane Doe")
            eTag("name").spaces()
            sTag("age")
            content("25")
            eTag("age").spaces()
            eTag("person").spaces()

            eTag("people")
        }
    }

    @Test
    fun svg() {
        tokenize(svg)
    }

    @Test
    fun testContentBeforeTag() {
        val source = "Pre-content<tag>Post-content</tag>"
        val tokens = tokenize(source)

        assertEquals(4, tokens.size, "Should have 4 tokens (Content, STag, Content, ETag)")

        assertEquals(Token(TokenType.Content, "Pre-content"), tokens[0], "First token should be a Content type with 'Pre-content' value")
        assertEquals(Token(TokenType.STag, "tag"), tokens[1], "Second token should be a STag with 'tag' value")
        assertEquals(Token(TokenType.Content, "Post-content"), tokens[2], "Third token should be a Content type with 'Post-content' value")
        assertEquals(Token(TokenType.ETag, "tag"), tokens[3], "Fourth token should be an ETag with 'tag' value")
    }

    @Test
    fun testSlashesNotFollowedByAngleBracket() {
        val source = "<tag/ someAttribute=\"value\">Content</tag>"
        val tokens = tokenize(source)

        assertEquals(3, tokens.size, "Should have 3 tokens (STag, Content, ETag)")

        assertEquals(Token(TokenType.STag, "tag/ someAttribute=\"value\""), tokens[0], "First token should be a STag with 'tag/ someAttribute=\"value\"' value")
        assertEquals(Token(TokenType.Content, "Content"), tokens[1], "Second token should be a Content type with 'Content' value")
        assertEquals(Token(TokenType.ETag, "tag"), tokens[2], "Third token should be an ETag with 'tag' value")
    }

    @Test
    fun testContentBetweenTags() {
        val source = "<tag>Content</tag>ContentBetweenTag<tag>"
        val tokens = tokenize(source)

        assertEquals(5, tokens.size, "Should have 5 tokens (STag, Content, ETag, Content, STag)")

        assertEquals(Token(TokenType.STag, "tag"), tokens[0], "First token should be a STag with 'tag' value")
        assertEquals(Token(TokenType.Content, "Content"), tokens[1], "Second token should be a Content type with 'Content' value")
        assertEquals(Token(TokenType.ETag, "tag"), tokens[2], "Third token should be a ETag with 'tag' value")
        assertEquals(Token(TokenType.Content, "ContentBetweenTag"), tokens[3], "Fourth token should be a Content type with 'ContentBetweenTag' value")
        assertEquals(Token(TokenType.STag, "tag"), tokens[4], "Fifth token should be a STag with 'tag' value")
    }

    @Test
    fun testSelfClosingTag() {
        val source = "<tag/>"
        val tokens = tokenize(source)

        assertEquals(1, tokens.size, "Should have 1 token (EmptyElemTag)")

        assertEquals(Token(TokenType.EmptyElemTag, "tag/"), tokens[0], "The only token should be an EmptyElemTag with 'tag/' value")
    }
}