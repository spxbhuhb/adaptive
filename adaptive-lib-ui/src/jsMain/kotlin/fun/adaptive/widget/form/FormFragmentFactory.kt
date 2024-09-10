/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.widget.form

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.widget.form.fragment.FormInputField

object FormFragmentFactory : FoundationFragmentFactory() {
    init {
        add("form:inputfield") { p,i -> FormInputField(p.adapter as `fun`.adaptive.ui.AuiAdapter, p, i) }
    }
}