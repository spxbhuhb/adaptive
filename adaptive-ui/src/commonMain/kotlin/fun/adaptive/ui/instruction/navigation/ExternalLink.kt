/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.render.event

@Adat
class ExternalLink(val href: String) : AdaptiveInstruction {

    fun openLink(event: UIEvent) {
        event.fragment.uiAdapter.openExternalLink(href)
    }

    override fun apply(subject: Any) {
        event(subject) { it.onClick = OnClick(this::openLink) }
    }

}