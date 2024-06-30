/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.markdown.fragment

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.markdown.parse.*
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.flowBox
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*

val linkColor = color(0x0000ffu)
val textColor = color(0x333333u)
val headerSizes = arrayOf(28.sp, 24.sp, 20.sp, 16.sp, 14.sp)

val codeStyles = arrayOf(
    paddingLeft(6.dp),
    paddingRight(6.dp),
    backgroundColor(color(0xe8e8e8u)),
    cornerRadius(6.dp),
    fontName("ui-monospace"),
    textColor
)

val textStyles = arrayOf(
    textColor
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
        name("markdown-header")
        marginBottom(20.dp)

        for (child in entry.children) {
            inline(entry.children[0], mutableListOf(headerSize(entry.level), bold))
        }
    }
}

@Adaptive
fun paragraph(entry: MarkdownParagraphAstEntry) {
    flowBox {
        AlignItems.bottom
        marginBottom(20.dp)
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
        }

        else -> text(entry.text, *formatting.toTypedArray(), *textStyles)
        // TODO reflink, refdef
    }
}

@Adaptive
fun inlineLink(entry: MarkdownInlineAstEntry, instructions: MutableList<AdaptiveInstruction>) {
    val label = entry.text.substringBefore(']').trim('[')
    val href = entry.text.substringAfter('(').trim(')')

    text(label, *instructions.toTypedArray(), linkColor, externalLink(href))
}
