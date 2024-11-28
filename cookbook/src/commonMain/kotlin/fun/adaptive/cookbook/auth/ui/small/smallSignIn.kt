/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.ui.small

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.cookbook.auth.model.SignIn
import `fun`.adaptive.cookbook.shared.darkGray
import `fun`.adaptive.cookbook.shared.footerLink
import `fun`.adaptive.cookbook.shared.mediumGray
import `fun`.adaptive.cookbook.shared.smallScreen
import `fun`.adaptive.cookbook.shared.subTitle
import `fun`.adaptive.cookbook.shared.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.snackbar.fail
import `fun`.adaptive.ui.snackbar.success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Adaptive
fun smallSignIn(): AdaptiveFragment {
    val signIn = copyStore { SignIn() }

    grid {
        smallScreen .. colTemplate(1.fr) .. rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)

        title("Welcome")

        subTitle("Sign in to continue")

        grid {
            colTemplate(1.fr) .. rowTemplate(52.dp repeat 2, 60.dp, 50.dp)

            editor { signIn.login } .. width { 315.dp }
            editor { signIn.password } .. width { 315.dp } // FIXME hard-coded width

            row {
                paddingTop(15.dp) .. spaceBetween .. maxWidth

                row {
                    checkbox { signIn.remember } .. paddingRight { 8.dp }
                    text("Remember be", fontSize(15.sp), textColor(darkGray))
                }

                text("Forgot password?", fontSize(15.sp), textColor(mediumGray), lightFont)
            }

            button("Sign In") .. maxWidth .. onClick {
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        getService<SessionApi>(adapter().transport).login(signIn.login, signIn.password)
                        success("Signed in!")
                    } catch (t: Throwable) {
                        fail("Sikertelen belépés")
                    }
                }
            }
        }

        footerLink("Don't have an account? ", "Sign Up", "/")
    }

    return fragment()
}