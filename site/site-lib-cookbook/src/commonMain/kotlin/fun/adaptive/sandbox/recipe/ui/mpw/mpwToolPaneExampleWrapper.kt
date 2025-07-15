package `fun`.adaptive.sandbox.recipe.ui.mpw

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.doc.model.GroveDocValueDomainDef.fragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.UUID

/**
 * This wrapper helps the documentation system to display the example.
 * Please ignore it.
 */
@Adaptive
fun mpwToolPaneExampleWrapper() : AdaptiveFragment {

    val workspace = fragment().firstContext<MultiPaneWorkspace>()

    val exampleToolPaneDef = PaneDef(
        uuid = UUID.nil(), // replace this with a static UUID like UUID("b9c83517-9c93-4463-ac47-bad531ffa491")
        name = "Example tool pane",
        icon = Graphics.example,
        position = PanePosition.LeftMiddle,
        fragmentKey = fragmentKey { "example:pane:tool" }
    )

    val backend = ExampleToolViewBackend(workspace, exampleToolPaneDef)

    column {
        maxWidth .. height { 120.dp } .. borders.outline

        localContext(backend) {
            mpwToolPaneExample()
        }
    }

    return fragment()
}