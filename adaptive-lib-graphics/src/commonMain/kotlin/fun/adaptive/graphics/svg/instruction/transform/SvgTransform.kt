/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.instruction.transform

import `fun`.adaptive.graphics.svg.parse.SvgInstruction
import `fun`.adaptive.graphics.svg.render.SvgRenderData
import `fun`.adaptive.utility.alsoIfInstance

interface SvgTransform : SvgInstruction {
    override fun apply(subject: Any) {
        subject.alsoIfInstance<SvgRenderData> {
            val transforms = it.transform
            if (transforms == null) {
                it.transform = mutableListOf(this)
            } else {
                transforms += this
            }
        }
    }
}