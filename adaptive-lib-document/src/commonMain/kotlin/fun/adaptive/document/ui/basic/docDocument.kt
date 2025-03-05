package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.processing.fetchAndCompile
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.document.DocumentResourceSet
import `fun`.adaptive.ui.api.text

@Adaptive
fun docDocument(resource : DocumentResourceSet) {

    val document = fetch { resource.fetchAndCompile() }
    val context = document?.let { DocRenderContext(it, it.styles.map { it.instructions }, DocumentTheme.DEFAULT) }

    if (context != null) {
        docBlock(context, document.blocks)
    }

}