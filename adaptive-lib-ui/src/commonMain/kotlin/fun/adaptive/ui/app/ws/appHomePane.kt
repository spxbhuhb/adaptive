package `fun`.adaptive.ui.app.ws

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.eco
import `fun`.adaptive.ui.builtin.home
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID

const val appHomeKey = "app:home"
val appHomeItem = SingularWsItem(Strings.home, appHomeKey)

fun Workspace.appHomePane() {

    val pane = WsPane(
        UUID(),
        Strings.home,
        Graphics.eco,
        WsPanePosition.Center,
        appHomeKey,
        appHomeItem,
        WsSingularPaneController(appHomeItem),
        singularity = WsPaneSingularity.SINGULAR,
        displayOrder = 0
    )

    toolPanes += pane

    addContentPaneBuilder(appHomeKey) { pane }
}