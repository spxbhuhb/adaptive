/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.resource.FileResource
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.utility.alsoIfInstance

fun externalLink(res : FileResource) = ExternalLink(res.uri)

fun externalLink(href : String) = ExternalLink(href)

data class ExternalLink(val href : String) : AdaptiveInstruction {

    fun openLink(event : AdaptiveUIEvent) {
        event.fragment.uiAdapter.openExternalLink(href)
    }

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderData> {
            it.onClick = OnClick(this::openLink)
        }
    }

}