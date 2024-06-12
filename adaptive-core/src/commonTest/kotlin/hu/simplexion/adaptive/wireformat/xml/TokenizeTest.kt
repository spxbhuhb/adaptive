/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.xml

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val basic = """
<people>
    <person id="1">
        <name>John Doe</name>
        <age>30</age>
    </person>
    <person id="2">
        <name>Jane Doe</name>
        <age>25</age>
    </person>
</people>
""".trimIndent()

private val svg = """
<svg id="thermometer-temperature-svgrepo-com" xmlns="http://www.w3.org/2000/svg" width="9.744" height="18.343" viewBox="0 0 9.744 18.343">
    <g id="Group_4" transform="translate(2.257)">
        <g id="Group_3">
            <path id="Path_1" d="M189.244,11.813V2.5a2.5,2.5,0,1,0-5,0v9.314a3.743,3.743,0,1,0,5,0ZM187.9,8.2h-2.313V2.5a1.156,1.156,0,1,1,2.313,0V8.2Z" transform="translate(-183.002)" fill="#1ad598"/>
        </g>
    </g>
    <g id="Group_6" transform="translate(0 8.501)">
        <g id="Group_5">
            <path id="Path_2" d="M121.815,237.268h-1.128a.671.671,0,0,0,0,1.342h1.128a.671.671,0,0,0,0-1.342Z" transform="translate(-120.016 -237.268)" fill="#1ad598"/>
        </g>
    </g>
    <g id="Group_8" transform="translate(0 6.29)">
        <g id="Group_7">
            <path id="Path_3" d="M121.815,175.566h-1.128a.671.671,0,1,0,0,1.342h1.128a.671.671,0,1,0,0-1.342Z" transform="translate(-120.016 -175.566)" fill="#1ad598"/>
        </g>
    </g>
    <g id="Group_10" transform="translate(0 4.079)">
        <g id="Group_9">
            <path id="Path_4" d="M121.815,113.864h-1.128a.671.671,0,0,0,0,1.342h1.128a.671.671,0,0,0,0-1.342Z" transform="translate(-120.016 -113.864)" fill="#1ad598"/>
        </g>
    </g>
</svg>
""".trimIndent()

class TokenizeTest {

    @Test
    fun basic() {
        val tokens = tokenizeXml(basic)

        var index = 0

        fun List<Token>.sTag(content : String): List<Token>  {
            assertEquals(Token(TokenType.STag, content), this[index++])
            return this
        }

        fun List<Token>.eTag(content : String) : List<Token>  {
            assertEquals(Token(TokenType.ETag, content), this[index++])
            return this
        }

        fun List<Token>.emptyElemTag(content : String) : List<Token>  {
            assertEquals(Token(TokenType.EmptyElemTag, content), this[index++])
            return this
        }

        fun List<Token>.content(content : String) : List<Token>  {
            assertEquals(Token(TokenType.Content, content), this[index++])
            return this
        }

        fun List<Token>.spaces() : List<Token> {
            assertTrue(this[index].value.isBlank())
            assertEquals(TokenType.Content, this[index++].type)
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
        tokenizeXml(svg)
    }
}