package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.app.appHomeKey
import `fun`.adaptive.ui.misc.todo

object AppFragmentFactory : FoundationFragmentFactory() {
    init {
        add(appHomeKey, ::appHome)
    }
}