/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter

@Suppress("EqualsOrHashCode")
class InputRenderData(
    val adapter : AbstractAuiAdapter<*,*>
) {
    var tabIndex : Int? = null

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is InputRenderData) return false

        if (tabIndex != other.tabIndex) return false

        return true
    }
}