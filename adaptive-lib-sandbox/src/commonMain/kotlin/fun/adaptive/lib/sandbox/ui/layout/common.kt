/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.layout/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.fragment.box
import `fun`.adaptive.ui.common.fragment.column
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.*

val black = Color(0x0u)
val outerBorder = Color(0xF08080u)
val innerBorder = Color(0xFFBF00u)

val blueishBackground = backgroundColor(Color(0xB0C4DEu))
val greenishBackground = backgroundColor(Color(0xB4E7B4u))
val orangeBackground = backgroundColor(Color(0xFFBF00u))

val trace = Trace()

// TODO remove = emptyArray() when #29 is fixed
@Adaptive
fun layoutExample(title: String, vararg instructions: AdaptiveInstruction = emptyArray(), @Adaptive example: () -> Unit): AdaptiveFragment {
    column(*instructions) {
        text(title)
        box {
            size(208.dp, 158.dp) .. border(outerBorder, 4.dp) .. name("example-container")
            example()
        }
    }

    return fragment()
}