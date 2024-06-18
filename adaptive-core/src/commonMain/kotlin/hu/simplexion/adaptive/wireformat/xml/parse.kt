/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.xml

import hu.simplexion.adaptive.utility.skipWhile
import hu.simplexion.adaptive.utility.peek
import hu.simplexion.adaptive.utility.pop
import hu.simplexion.adaptive.utility.push

fun parseXml(source: String, skipContent: Boolean = false, skipBlankContent: Boolean = true): XmlElement? {
    val tokens = tokenize(source)
    require(tokens.isNotEmpty()) { "Empty xml element" }

    val stack = mutableListOf<XmlElement>()
    var index = 0

    while (index < tokens.size) {
        val token = tokens[index]

        when (token.type) {
            TokenType.STag -> {
                val tag = token.value.substringBefore(' ')

                require(tag.isNotEmpty()) { "empty tag" }
                val firstChar = tag[0]
                if (firstChar == '?' || firstChar == '!') continue

                val attributes = attributes(token.value)
                val element = XmlElement(tag, attributes, mutableListOf())
                stack.push(element)
            }

            TokenType.Content -> {
                if (! skipContent) {
                    if (! skipBlankContent || token.value.isNotBlank()) {
                        val element = stack.peek()
                        element.children += XmlContent(token.value)
                    }
                }
            }

            TokenType.ETag -> {
                val element = stack.pop()
                require(element.tag == token.value) { "start and end tag mismatch: ${element.tag} vs ${token.value}" }
                if (stack.isEmpty()) {
                    return element
                }
                val parent = stack.peek()
                parent.children.add(element)
            }

            TokenType.EmptyElemTag -> {
                val attributes = attributes(token.value)
                val element = XmlElement(token.value.substringBefore(' '), attributes, mutableListOf())
                if (stack.isEmpty()) {
                    return element
                }
                val parent = stack.peek()
                parent.children.add(element)
            }

            TokenType.Other -> {
                // Ignore other types
            }
        }

        index ++
    }

    // If there are unbalanced tags, throw an exception
    if (stack.isNotEmpty()) {
        throw IllegalArgumentException("Unbalanced XML tags")
    }

    return null
}

internal fun attributes(tag: String): List<XmlAttribute> {
    val attributes = mutableListOf<XmlAttribute>()
    var index = tag.indexOf(' ')

    if (index == - 1) return attributes

    val len = tag.length

    while (index < len) {
        // find the start of the next attribute name (if there are any)
        index = tag.skipWhile(index, len) { it.isWhitespace() } ?: break
        val startName = index

        // find the end of the attribute name
        index = tag.skipWhile(startName, len) { ! it.isWhitespace() && it != '=' } ?: break
        val attributeName = tag.substring(startName, index)

        // skip any spaces between the name and the equal sign
        index = tag.skipWhile(index, len) { it.isWhitespace() } ?: break

        // any non '-' character is the next tag name
        if (tag[index] != '=') continue

        index ++ // move past the '='

        // skip any spaces between '=' and the quote
        index = tag.skipWhile(index, len) { it.isWhitespace() } ?: break

        val quote = tag[index]
        require(quote == '"' || quote == '\'') { "non quote character after '='" }

        index ++ // move past the starting quote
        val startValue = index

        index = tag.skipWhile(index, len) { it != quote } ?: throw IllegalArgumentException("missing closing quote")

        val attributeValue = tag.substring(startValue, index)
        index ++ // Move past the ending quote

        attributes += XmlAttribute(attributeName, attributeValue)
    }

    return attributes
}