/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.markdown.parse

interface MarkdownAstEntry

class MarkdownHeaderAstEntry(
    val level: Int,
    val children: List<MarkdownAstEntry>
) : MarkdownAstEntry

class MarkdownInlineAstEntry(
    val text: String,
    val bold: Boolean,
    val italic: Boolean,
    val code: Boolean = false,
    val inlineLink: Boolean = false,
    val referenceLink: Boolean = false,
    val referenceDef: Boolean = false,
) : MarkdownAstEntry

class MarkdownParagraphAstEntry(
    val children: MutableList<MarkdownAstEntry>,
    var closed: Boolean
) : MarkdownAstEntry

class MarkdownListAstEntry(
    val bullet: Boolean,
    val level: Int,
    val spaces: Int,
    val children: MutableList<MarkdownAstEntry>
) : MarkdownAstEntry

class MarkdownCodeFenceAstEntry(
    val language: String?,
    val content: String
) : MarkdownAstEntry

class MarkdownQuoteEntry(
    val level: Int,
    val children: MutableList<MarkdownAstEntry>
) : MarkdownAstEntry

class MarkdownHorizontalRuleAstEntry : MarkdownAstEntry