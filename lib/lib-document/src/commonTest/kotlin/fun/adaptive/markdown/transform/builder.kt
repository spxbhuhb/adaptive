package `fun`.adaptive.markdown.transform

import `fun`.adaptive.document.model.*

fun paragraph(standalone: Boolean = false, content: () -> String) =
    DocParagraph(- 1, listOf(DocText(0, content())), standalone)

fun blockImage(url : String, content: () -> String) =
    DocBlockImage(- 1, content(), url)

fun item(vararg path: Int, bullet: Boolean = true, label : String, content : (BlockBuilder.() -> Unit)? = null) =
    DocListItem(- 1, paragraph { label }, content?.let { list(false, content) }, path.toList(), bullet)

fun subList(vararg path: Int, bullet: Boolean = true, content: String, buildFun: (BlockBuilder.() -> Unit)?) =
    DocListItem(- 1, paragraph { content }, buildFun?.let { list(false, it) }, path.toList(), bullet)

fun list(standalone: Boolean = true, buildFun: BlockBuilder.() -> Unit) =
    DocList(- 1, BlockBuilder().apply { buildFun() }.entries.map { it as DocListItem }, standalone)

fun quote(buildFun: BlockBuilder.() -> Unit) =
    DocQuote(- 1, BlockBuilder().apply { buildFun() }.entries)

class BlockBuilder {
    val entries = mutableListOf<DocBlockElement>()

    operator fun DocBlockElement.unaryPlus() {
        entries.add(this)
    }
}

class DocBuilder {
    val entries = mutableListOf<DocBlockElement>()

    operator fun DocBlockElement.unaryPlus() {
        entries.add(this)
    }
}