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

@Adaptive
fun docDocument(
    resource : DocumentResourceSet,
    theme : DocumentTheme = DocumentTheme.default,
    arguments : AdatClass? = null
) : AdaptiveFragment {

    val document = fetch { resource.fetchAndCompile(theme) }

    val context = document?.let { doc ->
        DocRenderContext(
            document = doc,
            styles = doc.styles.map { it.instructions },
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