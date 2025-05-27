package `fun`.adaptive.app.ws.auth.account

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun wsAppAccountSelf() : AdaptiveFragment {

    val viewBackend = viewBackend(AccountSelfViewBackend::class)

    val accountEditorData = fetch { viewBackend.getAccountEditorData() } ?: AccountEditorData()

    accountEditorSelf(accountEditorData) { viewBackend.save(it) }

    return fragment()
}