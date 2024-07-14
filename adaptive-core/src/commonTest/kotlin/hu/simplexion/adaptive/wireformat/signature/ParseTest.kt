/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.signature

import kotlin.test.Test
import kotlin.test.assertEquals

class ParseTest {

    @Test
    fun tokenizeTest() {
        val actual = tokenizeSignature("Lkotlin/collections/List<Lkotlin/Pair<LA;[+I>;>;")
        val expected = listOf(
            Token(TokenType.Name, "kotlin/collections/List"),
            Token(TokenType.Open, ""),
            Token(TokenType.Name, "kotlin/Pair"),
            Token(TokenType.Open, ""),
            Token(TokenType.Name, "A"),
            Token(TokenType.Primitive, "[+I"),
            Token(TokenType.Close, ""),
            Token(TokenType.Close, ""),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun parseTest() {
        val actual = parseTypeSignature("Lkotlin/collections/List<Lkotlin/Pair<LA;[+I>;>;")
        val expected = WireFormatType(
            name = "kotlin/collections/List",
            generics = mutableListOf(
                WireFormatType(
                    name = "kotlin/Pair",
                    generics = mutableListOf(
                        WireFormatType("A"),
                        WireFormatType("[+I", short = true)
                    )
                )
            )
        )
        assertEquals(expected, actual)
    }
}