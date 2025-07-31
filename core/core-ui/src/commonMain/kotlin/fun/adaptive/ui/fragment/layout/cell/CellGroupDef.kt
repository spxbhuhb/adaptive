/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout.cell

import `fun`.adaptive.adat.Adat

/**
 * Cell group definition for use with [AbstractCellBox].
 *
 * @property label Label for the group (e.g., "V1", "V2")
 * @property priority Priority for responsive layout collapse (lower = more likely to collapse)
 */
@Adat
class CellGroupDef(
    val label: String,
    val priority: Int
)