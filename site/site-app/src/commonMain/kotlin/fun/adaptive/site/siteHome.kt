package `fun`.adaptive.site

import `fun`.adaptive.document.ui.basic.docDocument
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.resource.document.Documents
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.resource.image.Images
import `fun`.adaptive.site.app.generated.resources.*
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.loading.loading

@Adaptive
fun siteHome(): AdaptiveFragment {

    val media = mediaMetrics()

    val contentWidth = when {
        media.viewWidth < 600 -> 300.dp
        media.viewWidth < 800 -> 500.dp
        else -> 680.dp
    }

    val documentOrNull = fetch { Documents.home.readAll() }

    column {
        maxSize .. gap { 24.dp } .. paddingTop { 32.dp } .. verticalScroll

        loading(documentOrNull) {
            column {
                width { contentWidth } .. alignSelf.center
                docDocument(Documents.home)
            }
        }

        text("adaptive.fun does not use cookies") ..
            paddingTop { 32.dp } ..
            paddingBottom { 32.dp } ..
            alignSelf.center
    }

    return fragment()
}