package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.chevron_left
import `fun`.adaptive.ui.generated.resources.menu
import `fun`.adaptive.ui.generated.resources.more_vert
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.backends.PaneViewBackend
import kotlin.properties.Delegates.observable

@Adaptive
fun multiPaneWorkspaceSmall(
    workspace: MultiPaneWorkspace
) {
    val isFullScreen = observe { workspace.isFullScreen }
    val backend = observe { SmallWorkspaceBackend(workspace) }

    grid(instructions()) {
        if (isFullScreen) rowTemplate(0.dp, 1.fr) else rowTemplate(workspace.theme.smallHeaderHeight, 1.fr)

        if (! isFullScreen) {
            mpSmallHeader(backend)
        }

        when (backend.mode) {
            SmallMpwMode.ToolList -> mpSmallToolList(backend)
            SmallMpwMode.ToolArea -> mpSmallToolArea(backend)
            SmallMpwMode.Content -> mpSmallContent(backend)
        }
    }
}

class SmallWorkspaceBackend(
    val workspace: MultiPaneWorkspace
) : SelfObservable<SmallWorkspaceBackend>() {

    var mode by observable(SmallMpwMode.Content, ::notify)

    val theme
        get() = workspace.theme

    fun onMenuClick() {
        when (mode) {
            SmallMpwMode.ToolList -> mode = SmallMpwMode.Content
            SmallMpwMode.ToolArea -> mode = SmallMpwMode.ToolList
            SmallMpwMode.Content -> {
                if (workspace.focusedTool.value != null) {
                    mode = SmallMpwMode.ToolArea
                } else {
                    mode = SmallMpwMode.ToolList
                }
            }
        }
    }
}

enum class SmallMpwMode {
    ToolList,
    ToolArea,
    Content
}

@Adaptive
private fun mpSmallHeader(
    backend: SmallWorkspaceBackend
) {
    grid {
        colTemplate(48.dp, 1.fr, 48.dp)
        rowTemplate(backend.theme.smallHeaderHeight)

        box {
            size(48.dp) .. alignItems.center
            onClick { backend.onMenuClick() }
            if (backend.mode == SmallMpwMode.ToolArea) {
                actionIcon(Graphics.chevron_left)
            } else {
                actionIcon(Graphics.menu)
            }
        }

        todo("title")

        box {
            size(48.dp) .. alignItems.center
            actionIcon(Graphics.more_vert)
        }
    }
}

@Adaptive
private fun mpSmallToolList(
    backend: SmallWorkspaceBackend
) {
    column {
        maxSize .. scroll

        for (tool in backend.workspace.toolPanes) {
            row {
                height { backend.theme.smallToolItemHeight }
                onClick {
                    backend.workspace.focusedTool.value = tool.uuid
                    backend.mode = SmallMpwMode.ToolArea
                }

                box {
                    size(48.dp) .. alignItems.center
                    icon(tool.paneDef.icon) .. backend.theme.smallToolIcon
                }

                text(tool.paneDef.name) .. backend.theme.smallToolTitle
            }
        }
    }
}

@Adaptive
private fun mpSmallToolArea(
    backend: SmallWorkspaceBackend
) {
    val pane = backend.workspace.toolPanes.firstOrNull { it.uuid == backend.workspace.focusedTool.value }

    box(instructions()) {
        maxSize
        if (pane != null) {
            localContext(pane) {
                actualize(pane.paneDef.fragmentKey, null, emptyInstructions, pane)
            }
        } else {
            text("no content")
        }
    }

}

@Adaptive
private fun mpSmallContent(
    backend: SmallWorkspaceBackend
) {
    val paneBackend = backend.workspace.contentPaneGroups.value.firstOrNull()?.panes?.firstOrNull()

    if (paneBackend != null) {
        localContext(paneBackend) {
            actualize(paneBackend.paneDef.fragmentKey, null)
        }
    }
}