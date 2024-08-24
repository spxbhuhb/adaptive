import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.instruction.AdaptiveDetach
import `fun`.adaptive.foundation.instruction.DetachHandler
import `fun`.adaptive.foundation.instruction.DetachName
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.navigation.NavClick
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.api.rowTemplate

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */


val mainContent = name("main content")

fun replaceMain(
    @DetachName segment: String,
    @AdaptiveDetach content: (handler: DetachHandler) -> Unit
) = NavClick(mainContent, segment, content)

private val grid1fr = arrayOf(
    rowTemplate(1.fr),
    colTemplate(1.fr)
)

fun AdaptiveAdapter.switchTheme() {
    if (this !is AbstractAuiAdapter<*, *>) return
    if (mediaMetrics.isDark) {
        manualTheme = ThemeQualifier.LIGHT
    } else {
        manualTheme = ThemeQualifier.DARK
    }
}