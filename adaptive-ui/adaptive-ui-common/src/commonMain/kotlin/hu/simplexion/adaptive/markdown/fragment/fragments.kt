/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.markdown.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.markdown.parse.*
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

val linkColor = color(0x0000ffu)
val headerSizes = arrayOf(28.sp, 24.sp, 20.sp, 16.sp, 14.sp)

val codeStyles = arrayOf(
    paddingLeft(4.dp),
    paddingRight(4.dp),
    backgroundColor(color(0xe0e0e0u)),
    cornerRadius(2.dp),
    fontName("Courier New")
)

fun headerSize(level: Int) =
    fontSize(
        if (level < headerSizes.size) {
            headerSizes[level]
        } else {
            headerSizes.last()
        }
    )

@Adaptive
fun document(source: String) {
    val entries = ast(tokenize(source))

    column {
        AlignItems.start

        for (entry in entries) {
            when (entry) {
                is MarkdownParagraphAstEntry -> paragraph(entry)
                is MarkdownHeaderAstEntry -> header(entry)
            }
        }
    }
}

@Adaptive
fun header(entry: MarkdownHeaderAstEntry) {
    row {
        AlignItems.start
        for (child in entry.children) {
            inline(child, mutableListOf(headerSize(entry.level)))
        }
    }
}

@Adaptive
fun paragraph(entry: MarkdownParagraphAstEntry) {
    row {
        AlignItems.start
        for (child in entry.children) {
            inline(child)
        }
    }
}

@Adaptive
fun inline(
    entry: MarkdownInlineAstEntry,
    formatting: MutableList<AdaptiveInstruction> = mutableListOf(),
) {
    if (entry.bold) formatting += FontWeight.BOLD
    // TODO italic

    when {
        entry.code -> {
            text(entry.text, *formatting.toTypedArray(), *codeStyles)
        }

        entry.inlineLink -> {
            inlineLink(entry, formatting)
        }

        else -> text(entry.text, *formatting.toTypedArray())
        // TODO reflink, refdef
    }
}

@Adaptive
fun inlineLink(entry: MarkdownInlineAstEntry, instructions: MutableList<AdaptiveInstruction>) {
    val label = entry.text.substringBefore(']').trim('[')
    val href = entry.text.substringAfter('(').trim(')')

    text(label, *instructions.toTypedArray(), linkColor, externalLink(href))
}
