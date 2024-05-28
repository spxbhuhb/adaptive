/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.resource.DefaultResourceReader
import hu.simplexion.adaptive.resource.FileResource
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.utility.alsoIfInstance

fun externalLink(res : FileResource) = ExternalLink(res.uri)

fun externalLink(href : String) = ExternalLink(href)

class ExternalLink(val href : String) : AdaptiveInstruction {

    fun openLink(event : AdaptiveUIEvent) {
        (event.fragment.adapter as AdaptiveUIAdapter).openExternalLink(href)
    }

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> {
            it.onClick = OnClick(this::openLink)
        }
    }

}