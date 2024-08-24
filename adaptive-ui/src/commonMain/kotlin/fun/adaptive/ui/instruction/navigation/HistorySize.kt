/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.instruction.*

/**
 * Sets history size route for `slot` fragments. See the navigation
 * tutorial for details.
 */
@Adat
class HistorySize(
    val size: Int
) : AdaptiveInstruction