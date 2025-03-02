package `fun`.adaptive.markdown.compiler

import `fun`.adaptive.markdown.model.MarkdownElement
import `fun`.adaptive.markdown.model.MarkdownInline
import `fun`.adaptive.markdown.model.MarkdownList
import `fun`.adaptive.markdown.model.MarkdownListItem
import `fun`.adaptive.markdown.model.MarkdownParagraph
import `fun`.adaptive.markdown.model.MarkdownQuote

fun paragraph(content: () -> String) =
    MarkdownParagraph(mutableListOf(MarkdownInline(content(), bold = false, italic = false)), false)

fun item(level: Int = 1, bullet: Boolean = true, label : String, subBullet : Boolean = bullet, content: (ListBuilder.() -> Unit)? = null) =
    MarkdownListItem(bullet, level, paragraph { label }, content?.let { list(level + 1, subBullet, content) } )

fun list(level: Int = 1, bullet: Boolean = true, buildFun: ListBuilder.() -> Unit) =
    MarkdownList(bullet, level, ListBuilder().apply { buildFun() }.entries)

fun quote(buildFun: QuoteBuilder.() -> Unit) =
    MarkdownQuote(QuoteBuilder().apply { buildFun() }.entries)

class ListBuilder {
    val entries = mutableListOf<MarkdownListItem>()

    operator fun MarkdownListItem.unaryPlus() {
        entries.add(this)
    }
}

class QuoteBuilder {
    val entries = mutableListOf<MarkdownElement>()

    operator fun MarkdownElement.unaryPlus() {
        entries.add(this)
    }
}

class ASTBuilder {
    val entries = mutableListOf<MarkdownElement>()

    operator fun MarkdownElement.unaryPlus() {
        entries.add(this)
    }
}