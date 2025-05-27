package `fun`.adaptive.ui.mpw.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.mpw.fragments.toolPane
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun exampleContentPane(): AdaptiveFragment {

    val viewBackend = viewBackend(ExampleContentViewBackend::class)

    text("Example content: ${viewBackend.exampleContent}")

    return fragment()
}