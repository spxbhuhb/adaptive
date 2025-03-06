/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.basic.auth.ui.small

import `fun`.adaptive.adaptive_lib_app_basic.generated.resources.*
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.snackbar.successNotification

@Adaptive
fun passwordReset(): AdaptiveFragment {
    var email = ""

    grid {
        colTemplate(1.fr) .. rowTemplate(183.dp, 118.dp, 1.fr, 81.dp)

        h1(Strings.pwdResetTitle)

        h2(Strings.pwdResetSubTitle)

        grid {
            colTemplate(1.fr) .. rowTemplate(52.dp repeat 2, 60.dp, 50.dp)

            editor { email } .. inputPlaceholder { Strings.email } .. width(315.dp)

            button(Strings.pwdResetButton) .. maxWidth .. onClick {
                successNotification(Strings.pwdResetSendSuccess)
            }
        }

    }

    return fragment()
}