package `fun`.adaptive.app.ui.common.user.account

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch

@Adaptive
fun mpwAccountSelf() : AdaptiveFragment {

    val viewBackend = AccountSelfViewBackend(fragment())

    val accountEditorData = fetch { viewBackend.getAccountEditorData() } ?: AccountEditorData()

    accountEditorSelf(accountEditorData) { viewBackend.save(it) }

    return fragment()
}