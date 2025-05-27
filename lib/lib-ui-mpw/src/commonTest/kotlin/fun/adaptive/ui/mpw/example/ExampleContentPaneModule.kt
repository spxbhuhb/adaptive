package `fun`.adaptive.ui.mpw.example

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

class ExampleContentPaneModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

    val EXAMPLE_CONTENT_KEY = fragmentKey { "example:pane:content" }

    val EXAMPLE_CONTENT_TYPE = contentType { "example content type" }

    val exampleContentPaneDef = PaneDef(
        uuid = UUID.nil(), // replace this with a static UUID like UUID("b9c83517-9c93-4463-ac47-bad531ffa491")
        name = Strings.settings,
        icon = Graphics.settings,
        position = PanePosition.LeftMiddle,
        fragmentKey = EXAMPLE_CONTENT_KEY
    )

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(EXAMPLE_CONTENT_KEY, ::exampleContentPane) // register the UI fragment
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        addContentPaneBuilder(
            contentType = EXAMPLE_CONTENT_TYPE,
            condition = { it as? String },
            builder = { ExampleContentViewBackend(this, exampleContentPaneDef, it) }
        )

    }

}