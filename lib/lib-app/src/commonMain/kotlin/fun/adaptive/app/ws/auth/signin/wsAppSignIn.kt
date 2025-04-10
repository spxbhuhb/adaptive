/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.ws.auth.signin

import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.ws.wsApplication
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.app.AuthAppContext.Companion.authContext
import `fun`.adaptive.auth.model.basic.BasicSignIn
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.platform.input.findActualInputValue
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.model.WsPane
import kotlinx.coroutines.launch

@Adaptive
fun wsAppSignIn(@Suppress("unused") pane : WsPane<*,*>): AdaptiveFragment {

    val app = fragment().wsApplication

    val signIn = copyOf { BasicSignIn() }
    val signInFragment = fragment()

    box {
        maxSize .. alignItems.center .. backgrounds.surfaceVariant

        grid(name("signInForm")) {
            size((300 + 64).dp, (340 + 64).dp) .. colTemplate(1.fr) .. rowTemplate(38.dp, 88.dp, 60.dp, 60.dp, 100.dp)
            alignItems.center .. borders.outline .. cornerRadius { 8.dp } .. backgrounds.surface .. padding { 32.dp }

            h1(Strings.signInTitle)

            h2(Strings.signInSubTitle)

            editor { signIn.login } .. inputPlaceholder { Strings.email }
            textInput(signIn.password) {  } .. inputPlaceholder { Strings.password } .. secret .. name("signInPassword")

//            row {
//                alignSelf.startCenter
//                editor { signIn.remember } .. paddingRight { 8.dp }
//                text(Strings.signInRemember)
//            }

            button(Strings.signInButton) .. maxWidth .. marginLeft { 32.dp } .. marginRight { 32.dp } .. onClick {

                // have to use field values directly as Chrome autofill does not fire proper field change
                // event, see https://stackoverflow.com/questions/35049555/chrome-autofill-autocomplete-no-value-for-password

                val actualSignIn = BasicSignIn(
                    signIn.login,
                    findActualInputValue(signInFragment, "signInPassword"),
                    signIn.remember
                )

                if (actualSignIn.login.isEmpty() || actualSignIn.password.isEmpty()) {
                    warningNotification(Strings.invalidFields)
                    return@onClick
                }

                adapter().scope.launch {
                    try {
                        app.authContext.sessionOrNull = getService<AuthSessionApi>(adapter().transport).signIn(actualSignIn.login, actualSignIn.password)
                        app.onSignIn()
                    } catch (t: Throwable) {
                        t.printStackTrace()
                        failNotification(Strings.singInFail)
                    }
                }
            }

            // markdown(Strings.signInForgot)
        }
    }

    return fragment()
}