package `fun`.adaptive.app.ui.util

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.document.DocumentResourceSet
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun document(
    doc: DocumentResourceSet,
    argument: AdatClass? = null,
    theme: DocumentTheme = DocumentTheme.default
): AdaptiveFragment {

    column {
        maxSize .. verticalScroll
        gap { 12.dp } .. width { 600.dp }

        docDocument(doc, theme, argument)
    }

    return fragment()
}