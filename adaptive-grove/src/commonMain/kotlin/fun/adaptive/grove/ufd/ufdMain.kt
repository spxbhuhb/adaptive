/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.*
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.account_box
import `fun`.adaptive.ui.builtin.menu
import `fun`.adaptive.ui.builtin.settings
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.WorkspacePane
import `fun`.adaptive.ui.workspace.WorkspacePanePosition
import `fun`.adaptive.ui.workspace.wsFull
import `fun`.adaptive.utility.UUID

@Adaptive
fun ufdMain() {

    val controller = SheetViewController(false, true, true)
    controller.extensions += UfdContext()

    box {
        maxSize

        localContext(controller) {
            wsFull(buildWorkspace())
        }
    }

}

fun buildWorkspace(): Workspace {

    val workspace = Workspace()

    val palette = WorkspacePane(
        UUID(),
        "Palette",
        Graphics.palette,
        WorkspacePanePosition.LeftTop,
        "grove:ufd:palette"
    )

    val structure = WorkspacePane(
        UUID(),
        "Structure",
        Graphics.account_box,
        WorkspacePanePosition.LeftMiddle,
        "grove:ufd:structure",
    )

    val instructions = WorkspacePane(
        UUID(),
        "Left Middle - 2",
        Graphics.settings,
        WorkspacePanePosition.RightTop,
        "grove:ufd:instructions",
    )

    val center = WorkspacePane(
        UUID(),
        "Sheet",
        Graphics.menu,
        WorkspacePanePosition.Center,
        "grove:ufd:center"
    )

    workspace.panes.addAll(listOf(palette, structure, instructions, center))

    workspace.leftTop.value = palette.uuid
    workspace.leftMiddle.value = structure.uuid
    workspace.rightTop.value = instructions.uuid
    workspace.center.value = center.uuid

    return workspace
}