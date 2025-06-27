package `fun`.adaptive.grove.ufd.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.grove.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.grove.generated.resources.palette
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.HandleInfo
import `fun`.adaptive.grove.sheet.model.SheetClipboardItem
import `fun`.adaptive.grove.sheet.model.SheetSnapshot
import `fun`.adaptive.grove.sheet.operation.Add
import `fun`.adaptive.grove.sheet.operation.Copy
import `fun`.adaptive.grove.sheet.operation.Cut
import `fun`.adaptive.grove.sheet.operation.Group
import `fun`.adaptive.grove.sheet.operation.Move
import `fun`.adaptive.grove.sheet.operation.Paste
import `fun`.adaptive.grove.sheet.operation.Remove
import `fun`.adaptive.grove.sheet.operation.Resize
import `fun`.adaptive.grove.sheet.operation.SelectByFrame
import `fun`.adaptive.grove.sheet.operation.SelectByIndex
import `fun`.adaptive.grove.sheet.operation.SelectByPosition
import `fun`.adaptive.grove.sheet.operation.Undo
import `fun`.adaptive.grove.sheet.operation.Ungroup
import `fun`.adaptive.grove.ufd.ufdCenter
import `fun`.adaptive.grove.ufd.ufdComponents
import `fun`.adaptive.grove.ufd.ufdInstructions
import `fun`.adaptive.grove.ufd.ufdPalette
import `fun`.adaptive.grove.ufd.ufdState
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.menu
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.SideBarAction
import `fun`.adaptive.ui.mpw.backends.UnitPaneViewBackend
import `fun`.adaptive.ui.mpw.model.PaneDef
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4

class GroveUdfModuleMpw<FW : MultiPaneWorkspace, BW : AbstractWorkspace>() : AppModule<FW, BW>() {

    val palette = observableOf {
        listOf(
            LfmDescendant("aui:text", emptyInstructions, LfmMapping(dependencyMask = 0, LfmConst("T", "Text"))),
            LfmDescendant("aui:rectangle", instructionsOf(size(100.dp, 100.dp), border(colors.danger, 1.dp)))
        )
    }

    val focusedSheet = observableOf<SheetViewBackend?> { null }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(PALETTE_TOOL_KEY, ::ufdPalette)
        add(COMPONENTS_TOOL_KEY, ::ufdComponents)
        add(INSTRUCTIONS_TOOL_KEY, ::ufdInstructions)
        add(STATE_TOOL_KEY, ::ufdState)
        add(CONTENT_PANE_KEY, ::ufdCenter)
    }

    override fun frontendWorkspaceInit(workspace: FW, session: Any?) = with(workspace) {

        addToolPane {
            UnitPaneViewBackend(
                workspace,
                PaneDef(
                    UUID("9329e2a2-f761-4f7f-8469-865546dbfe70"),
                    Strings.palette,
                    Graphics.palette,
                    PanePosition.LeftMiddle,
                    PALETTE_TOOL_KEY
                )
            )
        }

        addContentPaneBuilder(
            WSIT_UFD_FRAGMENT, { type, item -> item as? LfmDescendant }
        ) { item ->
            SheetViewBackend(
                workspace,
                PaneDef(
                    uuid4(),
                    "",
                    Graphics.menu_book,
                    PanePosition.Center,
                    CONTENT_PANE_KEY
                ),
                item
            )
        }

        + SideBarAction(
            "stuff",
            Graphics.menu,
            PanePosition.LeftBottom,
            Int.MAX_VALUE - 1,
            null
        ) {
            addContent(WSIT_UFD_FRAGMENT, LfmDescendant("aui:rectangle", emptyInstructions, uuid = uuid4()))
        }

    }

    companion object {
        const val PALETTE_TOOL_KEY = "grove:ufd:pane:palette"
        const val COMPONENTS_TOOL_KEY = "grove:ufd:pane:components"
        const val INSTRUCTIONS_TOOL_KEY = "grove:ufd:pane:instructions"
        const val STATE_TOOL_KEY = "grove:ufd:pane:state"
        const val CONTENT_PANE_KEY = "grove:ufd:pane:content"

        const val WSIT_UFD_FRAGMENT = "grove:ufd:item:fragment"

        val AdaptiveFragment.udfModule
            get() = firstContext<MultiPaneWorkspace>().application.firstModule<GroveUdfModuleMpw<*, *>>()
    }
}