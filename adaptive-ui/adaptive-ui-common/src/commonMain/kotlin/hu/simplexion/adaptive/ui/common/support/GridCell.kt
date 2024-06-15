/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support

interface GridCell {
    var rowIndex: Int
    var colIndex: Int

    val gridRow : Int?
    val gridCol : Int?

    val rowSpan : Int
    val colSpan : Int
}