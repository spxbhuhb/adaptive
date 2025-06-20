/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.ui.common.user.signin

import `fun`.adaptive.auth.model.basic.BasicSignIn
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.lib_app.generated.resources.signInButton
import `fun`.adaptive.lib_app.generated.resources.signInSubTitle
import `fun`.adaptive.lib_app.generated.resources.signInTitle
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.textEditor
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.platform.input.findActualInputValue
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun signIn(): AdaptiveFragment {

    val viewBackend = SignInViewBackend(fragment())

    val template = BasicSignIn()
    val form = adatFormBackend(template)

    val signInFragment = fragment()

    val messageStore = storeFor { "" }
    val message = observe { messageStore }

    box {
        maxSize .. alignItems.center .. backgrounds.surfaceVariant

        localContext(form) {
            grid(name("signInForm")) {
                size((300 + 64).dp, (340 + 64).dp) .. colTemplate(1.fr) .. rowTemplate(38.dp, 88.dp, 60.dp, 60.dp, 52.dp, 52.dp)
                alignItems.center .. borders.outline .. cornerRadius { 8.dp } .. backgrounds.surface .. padding(32.dp, 32.dp, 16.dp, 32.dp)

                h1(Strings.signInTitle)

                h2(Strings.signInSubTitle)

                textEditor { template.accountName } .. focusFirst
                textEditor { template.password } .. name("signInPassword")

                box {
                    maxSize .. alignItems.center
                    if (message.isNotBlank()) {
                        text(message) .. textColors.fail .. fontSize { 14.sp } .. normalFont
                    }
                }

                box {
                    marginLeft { 32.dp } .. marginRight { 32.dp } .. marginTop { 16.dp } .. maxSize

                    submitButton(Strings.signInButton) .. maxWidth .. onClick {

                        if (form.isInvalid()) return@onClick

                        // have to use field values directly as Chrome autofill does not fire proper field change
                        // event, see https://stackoverflow.com/questions/35049555/chrome-autofill-autocomplete-no-value-for-password

                        val actualSignIn = BasicSignIn(
                            form.inputValue.accountName,
                            findActualInputValue(signInFragment, "signInPassword"),
                            form.inputValue.remember
                        )

                        viewBackend.signIn(actualSignIn) { messageStore.value = it }
                    }
                }

            }
        }
    }

    return fragment()
}