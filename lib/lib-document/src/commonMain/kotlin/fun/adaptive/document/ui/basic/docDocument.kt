package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.document.processing.fetchAndCompile
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.document.DocumentResourceSet
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text

@Adaptive
fun docDocument(
    resource : DocumentResourceSet,
    theme : DocumentTheme = DocumentTheme.DEFAULT,
    arguments : AdatClass? = null
) : AdaptiveFragment {

    val document = fetch { resource.fetchAndCompile() }

    val context = document?.let {
        DocRenderContext(
            document = it,
            styles = it.styles.map { it.instructions },
            theme = theme,
            arguments = arguments
        )
    }

    column(instructions()) {
        if (context != null) {
            docBlock(context, document.blocks)
        }
    }

    return fragment()
}