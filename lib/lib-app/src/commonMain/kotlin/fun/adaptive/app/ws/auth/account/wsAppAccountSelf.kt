package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.ui.mpw.model.PaneDef

@Adaptive
fun wsAppAccountSelf(pane : PaneDef<AccountSelfViewBackend>) : AdaptiveFragment {

    val accountEditorData = fetch { pane.viewBackend.getAccountEditorData() } ?: AccountEditorData()

    accountEditorSelf(accountEditorData) { pane.viewBackend.save(it) }

    return fragment()
}