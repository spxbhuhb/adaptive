/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package layout/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.Trace
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.instruction.*

val black = Color(0x0u)
val outerBorder = Color(0xF08080u)
val innerBorder = Color(0xFFBF00u)

val blueishBackground = backgroundColor(Color(0xB0C4DEu))
val greenishBackground = backgroundColor(Color(0xB4E7B4u))
val trace = Trace()

@Adaptive
fun layoutExample(@Adaptive example: () -> Unit) {
    box(size(208.dp, 158.dp), border(outerBorder, 4.dp), name("example-container")) {
        example()
    }
}