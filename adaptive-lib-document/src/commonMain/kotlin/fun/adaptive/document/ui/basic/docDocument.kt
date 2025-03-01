package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.*
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.Adaptive

@Adaptive
fun docDocument(doc: DocDocument) {
    val context = DocRenderContext(doc, doc.styles.map { it.instructions }, DocumentTheme.Companion.DEFAULT)

    docBlock(context, doc.children)

}