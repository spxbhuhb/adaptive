package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.app.ws.appAccountKey
import `fun`.adaptive.ui.app.ws.appHomeKey

object AppFragmentFactory : FoundationFragmentFactory() {
    init {
        add(appHomeKey, ::appHome)
        add(appAccountKey, ::appAccount)
    }
}