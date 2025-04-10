package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.content_copy
import `fun`.adaptive.ui.builtin.copied
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.platform.clipboard.copyToClipboard
import `fun`.adaptive.utility.UUID

@Adaptive
fun uuidLabel(theme: LabelTheme = LabelTheme.DEFAULT, uuid: () -> UUID<*>) {
    row {
        theme.uuidLabelContainer
        text(uuid()) .. theme.uuidLabelText
        actionIcon(Graphics.content_copy, theme = theme.copyIconTheme) ..
            onClick(Strings.copied) {
                copyToClipboard(uuid().toString())
            }
    }
}