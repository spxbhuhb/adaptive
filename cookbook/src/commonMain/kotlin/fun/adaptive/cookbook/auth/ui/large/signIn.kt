/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.cookbook.auth.ui.large

import `fun`.adaptive.adat.api.isTouched
import `fun`.adaptive.adat.api.touchAndValidate
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.auth.api.SessionApi
import `fun`.adaptive.cookbook.auth.model.SignIn
import `fun`.adaptive.cookbook.shared.largeScreen
import `fun`.adaptive.cookbook.shared.mediumGray
import `fun`.adaptive.cookbook.shared.titleLarge
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.foundation.rangeTo
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
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.semiBoldFont
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.smallCaps
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.snackbar.fail
import `fun`.adaptive.ui.snackbar.success
import `fun`.adaptive.ui.snackbar.warning
import kotlinx.coroutines.launch

@Adaptive
fun signIn(): AdaptiveFragment {
    val signIn = copyStore { SignIn() }

    box {
        largeScreen .. alignItems.center

        grid {
            size(377.dp, 600.dp) .. colTemplate(1.fr) .. rowTemplate(38.dp, 88.dp, 60.dp, 60.dp, 60.dp, 100.dp, 50.dp)
            alignItems.center

            text("Üdvözlünk") .. letterSpacing(0.03) .. titleLarge .. semiBoldFont .. smallCaps

            text("Lépj be folytatáshoz.")

            editor { signIn.login } .. inputPlaceholder { "e-mail cím" }

            editor { signIn.password } .. inputPlaceholder { "jelszó" }

            row {
                alignSelf.startCenter
                checkbox { signIn.remember } .. paddingRight { 8.dp }
                text("Emlékezzünk rád")
            }

            button("Belépés") .. maxWidth .. onClick {

                if (! signIn.touchAndValidate()) {
                    warning("Vannak még érvénytelen mezők.")
                    return@onClick
                }

                adapter().scope.launch {
                    try {
                        getService<SessionApi>(adapter().transport).login(signIn.login, signIn.password)
                        success("Sikeres belépés!")
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