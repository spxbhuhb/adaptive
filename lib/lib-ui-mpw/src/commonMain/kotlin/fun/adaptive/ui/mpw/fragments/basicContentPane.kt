package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.mpw.MultiPaneTheme
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue

@Adaptive
fun basicContentPane(
    title: String,
    uuid: UUID<*>? = null,
    value: AvValue<*>? = null,
    @Adaptive
    _fixme_adaptive_actions: () -> Unit,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    padding { 16.dp }

    column {
        MultiPaneTheme.DEFAULT.contentPaneContainer

        contentPaneHeader(title, uuid, value, _fixme_adaptive_actions)

        _fixme_adaptive_content()
    }

}