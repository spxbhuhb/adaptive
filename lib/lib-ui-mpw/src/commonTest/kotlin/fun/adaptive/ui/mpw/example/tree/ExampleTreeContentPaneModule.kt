package `fun`.adaptive.ui.mpw.example.tree

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.util.asValueOrNull

class ExampleTreeContentPaneModule<WT : MultiPaneWorkspace> : AppModule<WT>() {

    val EXAMPLE_CONTENT_KEY = fragmentKey { "example:pane:content" }

    val EXAMPLE_CONTENT_TYPE = contentType { "example content type" }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(EXAMPLE_CONTENT_KEY, ::exampleContentPane) // register the UI fragment
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {

        val exampleContentPaneDef = PaneDef(
            uuid = UUID.nil(), // replace this with a static UUID like UUID("b9c83517-9c93-4463-ac47-bad531ffa491")
            name = Strings.settings,
            icon = Graphics.settings,
            position = PanePosition.LeftMiddle,
            fragmentKey = EXAMPLE_CONTENT_KEY
        )

        addContentPaneBuilder(
            contentType = EXAMPLE_CONTENT_TYPE,
            condition = { asValueOrNull<ExampleTreeValueSpec>(it, avDomain.node) },
            builder = { ExampleTreeContentViewBackend(this, exampleContentPaneDef, it) }
        )

    }

}