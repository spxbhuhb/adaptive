package `fun`.adaptive.app.basic.auth.ui

import `fun`.adaptive.document.ui.DocumentTheme
import kotlin.io.encoding.Base64

class BasicAppTheme(
    documentTheme: DocumentTheme
) {

    val h1 = documentTheme.h1

    val h2 = documentTheme.h2

    companion object {
        val DEFAULT = BasicAppTheme(DocumentTheme.DEFAULT)
    }

}