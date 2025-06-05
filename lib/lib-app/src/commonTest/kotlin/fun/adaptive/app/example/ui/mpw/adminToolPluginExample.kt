package `fun`.adaptive.app.example.ui.mpw

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text

@Adaptive
fun adminToolPluginExample() : AdaptiveFragment {

    val viewBackend = AdminToolPluginViewBackendExample(fragment())
    val message = viewBackend.exampleFun()

    text("From the admin tool plugin: $message")

    return fragment()
}