package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsToolOrNull
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun docContentPane(): AdaptiveFragment {

    val viewBackend = viewBackend(DocContentViewBackend::class)

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        pageHeader(viewBackend.item)

        markdown(viewBackend.item.spec)
    }

    return fragment()
}

@Adaptive
fun pageHeader(value : GroveDocValue) {
    column {
        paddingBottom { 32.dp }
        h2(value.nameLike)
        itemPath(value)
    }
}

@Adaptive
fun itemPath(item: GroveDocValue) {
    val names = fragment().wsToolOrNull<DocToolViewBackend>()?.docPathNames(item) ?: emptyList()

    row {
        alignItems.center

        for (name in names.indices) {
            text(names[name]) .. textColors.onSurfaceVariant
            if (name < names.size - 1) {
                icon(Graphics.arrow_right) .. textColors.onSurfaceVariant
            }
        }
    }

}
