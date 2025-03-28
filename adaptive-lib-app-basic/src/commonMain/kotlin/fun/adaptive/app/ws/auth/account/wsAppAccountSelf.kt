package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.app.UiClientApplication.Companion.uiApplication
import `fun`.adaptive.app.basic.auth.ui.accountEditor
import `fun`.adaptive.app.ws.auth.BasicAppAuthWsModule.Companion.getAccountEditorData
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsAppAccountSelf(pane : WsPane<*,*>) : AdaptiveFragment {

    val app = fragment().uiApplication
    val accountEditorData = fetch { app.getAccountEditorData() }

    accountEditor(accountEditorData) { }

    return fragment()
}