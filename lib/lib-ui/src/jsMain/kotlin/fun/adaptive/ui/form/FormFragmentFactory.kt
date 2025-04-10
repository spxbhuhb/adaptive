/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.form

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.form.fragment.FormTextual

object FormFragmentFactory : FoundationFragmentFactory() {
    init {
        add("form:textual") { p, i, s -> FormTextual(p.adapter as AuiAdapter, p, i) }
    }
}