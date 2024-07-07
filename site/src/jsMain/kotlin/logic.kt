import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.instruction.AdaptiveDetach
import hu.simplexion.adaptive.foundation.instruction.DetachHandler
import hu.simplexion.adaptive.foundation.instruction.DetachName
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.resource.ThemeQualifier
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.NavClick
import hu.simplexion.adaptive.ui.common.instruction.colTemplate
import hu.simplexion.adaptive.ui.common.instruction.fr
import hu.simplexion.adaptive.ui.common.instruction.rowTemplate

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
    if (this !is AbstractCommonAdapter<*, *>) return
    if (mediaMetrics.isDark) {
        manualTheme = ThemeQualifier.LIGHT
    } else {
        manualTheme = ThemeQualifier.DARK
    }
}