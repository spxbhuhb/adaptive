/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.markdown.parse.*
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.externalLink
import `fun`.adaptive.ui.api.fontName
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.marginBottom
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.*

val linkColor = textColor(0x0000ff)
val textColor = textColor(0x333333)
val headerSizes = arrayOf(28.sp, 24.sp, 20.sp, 16.sp, 14.sp)

val codeStyles = instructionsOf(
    paddingLeft(6.dp),
    paddingRight(6.dp),
    backgroundColor(0xe8e8e8),
    cornerRadius(6.dp),
    fontName("ui-monospace"),
    textColor
)

val textStyles = instructionsOf(
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
            inline(entry.children[0], mutableListOf(headerSize(entry.level), boldFont))
        }
    }
}

@Adaptive
fun paragraph(entry: MarkdownParagraphAstEntry) {
    flowBox {
        alignItems.bottom
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
    if (entry.bold) formatting += boldFont
    // TODO italic

    when {
        entry.code -> {
            text(entry.text, *formatting.toTypedArray(), codeStyles)
        }

        entry.inlineLink -> {
        }

        else -> text(entry.text, *formatting.toTypedArray(), textStyles)
        // TODO reflink, refdef
    }
}

@Adaptive
fun inlineLink(entry: MarkdownInlineAstEntry, instructions: MutableList<AdaptiveInstruction>) {
    val label = entry.text.substringBefore(']').trim('[')
    val href = entry.text.substringAfter('(').trim(')')

    text(label, AdaptiveInstructionGroup(instructions), linkColor, externalLink(href))
}
