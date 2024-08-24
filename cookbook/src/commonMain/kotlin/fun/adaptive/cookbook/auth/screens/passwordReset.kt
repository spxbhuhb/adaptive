/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.screens

import `fun`.adaptive.cookbook.shared.button
import `fun`.adaptive.cookbook.shared.inputStyle
import `fun`.adaptive.cookbook.shared.mobileScreen
import `fun`.adaptive.cookbook.shared.subTitle
import `fun`.adaptive.cookbook.shared.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.input
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.api.inputPlaceholder

@Adaptive
fun passwordReset(): AdaptiveFragment {
    var email = ""

    grid {
        mobileScreen .. colTemplate(1.fr) .. rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)

        title("Reset Password")

        subTitle("Enter your e-mail address and we will send you the instructions on how to reset your password.")

        grid {
            colTemplate(1.fr) .. rowTemplate(52.dp repeat 2, 60.dp, 50.dp)

            input { email } .. inputStyle .. inputPlaceholder { "email" }

            button("Send") .. maxWidth .. onClick { println("send password reset") }
        }

    }

    return fragment()
}