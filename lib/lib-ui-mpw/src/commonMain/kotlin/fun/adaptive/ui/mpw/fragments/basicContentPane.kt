package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue

@Adaptive
fun basicContentPane(
    title: String,
    uuid: UUID<*>? = null,
    value: AvValue<*>? = null,
    _fixme_adaptive_actions: @Adaptive () -> Unit,
    _fixme_adaptive_content: @Adaptive () -> Unit
) : AdaptiveFragment {

    column {
        MultiPaneTheme.DEFAULT.contentPaneContainer .. instructions()

        contentPaneHeader(title, uuid, value, _fixme_adaptive_actions)

        _fixme_adaptive_content()
    }

    return fragment()
}