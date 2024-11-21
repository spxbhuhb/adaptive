package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.utility.localTime
import `fun`.adaptive.utility.p02

/**
 * Displays active snacks.
 */
@Adaptive
fun snackList(
    snacks: List<Snack>,
    theme: SnackbarTheme = snackbarTheme,
    vararg instructions: AdaptiveInstruction,
): AdaptiveFragment {
    column(*theme.container, *instructions) {
        height { (theme.snackHeight + theme.snackGap) * snacks.size }

        for (snack in snacks.sortedByDescending { it.createdAt }) {
            snackItem(snack, theme)
        }
    }

    return fragment()
}

@Adaptive
fun snackItem(snack: Snack, theme: SnackbarTheme) {
    row(*theme.item(snack)) {
        text(snack.message) .. theme.text(snack)
        text(snack.createdAt.localTime().let { "${it.hour.p02}:${it.minute.p02}" }) .. theme.text(snack)
    }
}