/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.fragment

import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.common.common

@AdaptiveExpect(common)
fun image(res: DrawableResource, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    manualImplementation(res, instructions)
}