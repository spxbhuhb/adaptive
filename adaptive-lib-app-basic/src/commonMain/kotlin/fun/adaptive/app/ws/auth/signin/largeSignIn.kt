/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.ws.auth.signin

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.adat.api.touchAndValidate
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.UiClientApplication
import `fun`.adaptive.app.UiClientApplication.Companion.uiApplication
import `fun`.adaptive.app.UiClientApplicationData
import `fun`.adaptive.app.basic.auth.model.BasicSignIn
import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.app.ws.BasicAppWsModule.Companion.wsApplication
import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.warningNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.utility.firstInstance
import kotlinx.coroutines.launch

@Adaptive
fun largeSignIn(): AdaptiveFragment {

    val app = fragment().wsApplication
    val basicAppWsModule = app.modules.firstInstance<BasicAppWsModule<*>>()

    val signIn = copyOf { BasicSignIn() }

    box {
        maxSize .. alignItems.center .. backgrounds.surfaceVariant

        grid {
            size((377 + 64).dp, (500 + 64).dp) .. colTemplate(1.fr) .. rowTemplate(38.dp, 88.dp, 60.dp, 60.dp, 60.dp, 100.dp, 50.dp)
            alignItems.center .. borders.outline .. cornerRadius { 8.dp } .. backgrounds.surface .. padding { 32.dp }

            h1(Strings.signInTitle)

            h2(Strings.signInSubTitle)

            editor { signIn.login } .. inputPlaceholder { Strings.email }
            editor { signIn.password } .. inputPlaceholder { Strings.password }

            row {
                alignSelf.startCenter
                editor { signIn.remember } .. paddingRight { 8.dp }
                text(Strings.signInRemember)
            }

            button(Strings.signInButton) .. maxWidth .. onClick {

                if (! signIn.touchAndValidate()) {
                    warningNotification(Strings.invalidFields)
                    return@onClick
                }

                adapter().scope.launch {
                    try {
                        app.appData.sessionOrNull = getService<SessionApi>(adapter().transport).login(signIn.login, signIn.password)
                        app.workspace.addContent(basicAppWsModule.HOME_CONTENT_ITEM)
                        app.onSignInSuccess()
                    } catch (t: Throwable) {
                        t.printStackTrace()
                        failNotification(Strings.singInFail)
                    }
                }
            }

            markdown(Strings.signInForgot)
        }
    }

    return fragment()
}