package `fun`.adaptive.app.example.ui.mpw

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

class AdminToolPluginViewBackendExample(fragment: AdaptiveFragment) {

    val workspace = fragment.firstContext<MultiPaneWorkspace>()

    fun exampleFun() = "Hello World!"

}