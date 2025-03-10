package `fun`.adaptive.grove.ufd

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsContext

class UfdContext(
    override val workspace: Workspace
) : WsContext {

    val palette = autoCollectionOrigin(
        listOf(
            LfmDescendant("aui:text", emptyInstructions, LfmMapping(dependencyMask = 0, LfmConst("T", "Text"))),
            LfmDescendant("aui:rectangle", instructionsOf(size(100.dp, 100.dp), border(colors.danger, 1.dp)))
        )
    )

}