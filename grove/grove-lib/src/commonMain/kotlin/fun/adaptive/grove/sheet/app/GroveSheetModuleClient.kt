package `fun`.adaptive.grove.sheet.app

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
import `fun`.adaptive.grove.sheet.fragment.GroveDrawingLayer
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
import `fun`.adaptive.ui.AbstractAuiAdapter
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
import `fun`.adaptive.wireformat.WireFormatRegistry

class GroveSheetModuleClient<FW : MultiPaneWorkspace, BW : AbstractWorkspace>() : AppModule<FW, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + Add
        + Copy
        + Cut
        + Group
        + Move
        + Paste
        + Remove
        + Resize
        + SelectByFrame
        + SelectByIndex
        + SelectByPosition
        + Undo
        + Ungroup

        + HandleInfo
        + SheetSnapshot
        + SheetClipboardItem
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add("grove:drawinglayer") { p, i, s -> GroveDrawingLayer(p.adapter as AbstractAuiAdapter<*,*>, p, i) }
    }

}