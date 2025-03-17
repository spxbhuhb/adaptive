/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.layout

/**
 * Strategy that changes container calculation behaviour.
 *
 * When [Constrain],
 *
 * [Constrain]
 */
enum class FillStrategy {
    /**
     * Stack containers (row, column) propose only the remaining space for their
     * content in respect of the content already placed.
     */
    Constrain,

    /**
     * Stack containers (row, column) propose only the remaining space for their
     * content in respect of the content that follows them.
     */
    ConstrainReverse
}