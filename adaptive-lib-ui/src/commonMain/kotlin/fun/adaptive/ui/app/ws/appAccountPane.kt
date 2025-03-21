package `fun`.adaptive.ui.app.ws

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.account
import `fun`.adaptive.ui.builtin.account_circle
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.model.WsPaneSingularity
import `fun`.adaptive.utility.UUID

const val appAccountKey = "app:account"
val appAccountItem = SingularWsItem(Strings.account, appAccountKey)

fun Workspace.appAccountPane() {

    val pane = WsPane(
        UUID(),
        Strings.account,
        Graphics.account_circle,
        WsPanePosition.LeftBottom,
        appAccountKey,
        appAccountItem,
        WsSingularPaneController(appAccountItem),
        singularity = WsPaneSingularity.SINGULAR,
        displayOrder = Int.MAX_VALUE - 1
    )

    toolPanes += pane

    addContentPaneBuilder(appAccountKey) { pane }

}