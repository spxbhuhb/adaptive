/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.ui.small

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.auth.model.SignUp
import `fun`.adaptive.cookbook.shared.black
import `fun`.adaptive.cookbook.shared.darkGray
import `fun`.adaptive.cookbook.shared.footerLink
import `fun`.adaptive.cookbook.shared.smallScreen
import `fun`.adaptive.cookbook.shared.subTitle
import `fun`.adaptive.cookbook.shared.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.externalLink
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.*

@Adaptive
fun signUp(): AdaptiveFragment {
    val signUp = copyStore { SignUp() }

    grid {
        smallScreen .. colTemplate(1.fr) .. rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)

        title("Welcome")

        subTitle("Sign up to join")

        grid {
            colTemplate(1.fr) .. rowTemplate(52.dp repeat 4, 60.dp, 50.dp)

            editor { signUp.name } .. width { 315.dp }
            editor { signUp.email } .. width { 315.dp }
            editor { signUp.password } .. width { 315.dp }
            editor { signUp.verification } .. width { 315.dp }

            grid {
                paddingTop(15.dp) .. alignItems.start .. colTemplate(40.dp, 1.fr)

                checkbox { signUp.agreement }
                row {
                    text("I agree to the ", fontSize(15.sp), textColor(darkGray))
                    text("Terms of Service", fontSize(15.sp), textColor(black), boldFont, externalLink("/terms.txt"))
                }
            }

            button("Sign Up") .. maxWidth .. onClick { println("sing up") }
        }

        footerLink("Have an account? ", "Sign in", "/")
    }

    return fragment()
}
