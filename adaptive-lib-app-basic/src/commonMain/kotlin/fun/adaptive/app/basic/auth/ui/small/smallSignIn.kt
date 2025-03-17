/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.basic.auth.ui.small

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.basic.auth.model.BasicSignIn
import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.successNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Adaptive
fun smallSignIn(): AdaptiveFragment {
    val signIn = copyOf { BasicSignIn() }

    grid {
        colTemplate(1.fr) .. rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)

        h1(Strings.signInTitle)

        h2(Strings.signInSubTitle)

        grid {
            colTemplate(1.fr) .. rowTemplate(52.dp repeat 2, 60.dp, 50.dp)

            editor { signIn.login } .. inputPlaceholder { Strings.email } .. maxWidth
            editor { signIn.password } .. inputPlaceholder { Strings.password } .. maxWidth

            row {
                paddingTop(15.dp) .. spaceBetween .. maxWidth

                row {
                    editor { signIn.remember } .. paddingRight { 8.dp }
                    text(Strings.signInRemember)
                }

                markdown(Strings.signInForgot)
            }

            button(Strings.signInButton) .. maxWidth .. onClick {
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        getService<SessionApi>(adapter().transport).login(signIn.login, signIn.password)
                        successNotification(Strings.signInSuccess)
                    } catch (t: Throwable) {
                        t.printStackTrace()
                        failNotification(Strings.singInFail)
                    }
                }
            }
        }

        markdown(Strings.signInSignUp)
    }

    return fragment()
}