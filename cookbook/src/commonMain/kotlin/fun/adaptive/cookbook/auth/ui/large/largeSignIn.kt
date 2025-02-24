/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.ui.large

import `fun`.adaptive.adat.api.touchAndValidate
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.cookbook.appData
import `fun`.adaptive.cookbook.auth.model.SignIn
import `fun`.adaptive.cookbook.shared.cornerRadius8
import `fun`.adaptive.cookbook.shared.mediumGray
import `fun`.adaptive.cookbook.shared.titleLarge
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.inputPlaceholder
import `fun`.adaptive.ui.api.letterSpacing
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.smallCaps
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.snackbar.fail
import `fun`.adaptive.ui.snackbar.warning
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import kotlinx.coroutines.launch

@Adaptive
fun largeSignIn(): AdaptiveFragment {
    val signIn = copyOf { SignIn() }

    box {
        maxSize .. alignItems.center

        grid {
            size((377 + 64).dp, (500 + 64).dp) .. colTemplate(1.fr) .. rowTemplate(38.dp, 88.dp, 60.dp, 60.dp, 60.dp, 100.dp, 50.dp)
            alignItems.center .. borders.outline .. cornerRadius8 .. backgrounds.surface .. padding { 32.dp }

            text("Üdvözlünk") .. letterSpacing(0.03) .. titleLarge .. semiBoldFont .. smallCaps

            text("Lépj be folytatáshoz.")

            editor { signIn.login } .. inputPlaceholder { "e-mail cím" }

            editor { signIn.password } .. inputPlaceholder { "jelszó" }

            row {
                alignSelf.startCenter
                editor { signIn.remember } .. paddingRight { 8.dp }
                text("Emlékezzünk rád")
            }

            button("Belépés") .. maxWidth .. onClick {

                if (! signIn.touchAndValidate()) {
                    warning("Vannak még érvénytelen mezők.")
                    return@onClick
                }

                adapter().scope.launch {
                    try {
                        appData.session = getService<SessionApi>(adapter().transport).login(signIn.login, signIn.password)
                        appData.onLoginSuccess()
                    } catch (t: Throwable) {
                        fail("Sikertelen belépés!")
                    }
                }
            }

            text("Elfelejtetted a jelszavad?", fontSize(15.sp), textColor(mediumGray), lightFont) .. noSelect .. onClick {

            }
        }
    }

    return fragment()
}