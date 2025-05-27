package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.ui.mpw.MultiPaneTheme.Companion.DEFAULT
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef

@Adaptive
fun toolPane(
    viewBackend: PaneViewBackend<*>,
    theme: MultiPaneTheme = DEFAULT,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    val hover = hover()

    grid {
        theme.toolPaneContainer

        paneTitle(viewBackend, hover, theme)

        box {
            theme.toolPaneContent
            _fixme_adaptive_content()
        }
    }
}