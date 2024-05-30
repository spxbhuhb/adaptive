/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.utility.alsoIfInstance

data class Color(val value: Int) : AdaptiveInstruction {

    override fun apply(subject: Any) {
        subject.alsoIfInstance<RenderInstructions> { it.color = this }
    }

    /**
     * @return [value] in "#ffffff" format
     */
    fun toHexColor() : String =
        "#${value.toString(16).padStart(6, '0')}"

}
