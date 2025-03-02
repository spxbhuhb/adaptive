/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

/**
 * Strategy that changes container calculation behaviour. When [Constrain], the
 * stack containers propose only the remaining space for their content in respect
 * of the content already placed.
 */
enum class FillStrategy {
    Constrain
}