package `fun`.adaptive.ui.mpw.example.tree

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun exampleContentPane(): AdaptiveFragment {

    val viewBackend = viewBackend(ExampleTreeContentViewBackend::class)

    text("Example message: ${viewBackend.content?.spec?.message}")

    return fragment()
}