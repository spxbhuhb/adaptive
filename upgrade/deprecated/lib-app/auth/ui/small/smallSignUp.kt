/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.basic.auth.ui.small

import `fun`.adaptive.lib_app.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.basic.auth.model.BasicSignUp
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adaptive
fun signUp(): AdaptiveFragment {
    val signUp = copyOf { BasicSignUp() }

    grid {
        colTemplate(1.fr) .. rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)

        h1(Strings.signUpTitle)

        h2(Strings.signUpSubTitle)

        grid {
            colTemplate(1.fr) .. rowTemplate(52.dp repeat 4, 60.dp, 50.dp)

            editor { signUp.name } .. width { 315.dp }
            editor { signUp.email } .. width { 315.dp }
            editor { signUp.password } .. width { 315.dp }
            editor { signUp.verification } .. width { 315.dp }

            grid {
                paddingTop(15.dp) .. alignItems.start .. colTemplate(40.dp, 1.fr)

                editor { signUp.agreement }

                markdown(Strings.signUpAgree)
            }

            button(Strings.signUpButton) .. maxWidth .. onClick {
                println("sing up")
            }
        }

       markdown(Strings.signUpSignIn)
    }

    return fragment()
}
