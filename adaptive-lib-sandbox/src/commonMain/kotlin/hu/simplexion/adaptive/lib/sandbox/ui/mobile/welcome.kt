/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui.mobile

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.foundation.instruction.instructionsOf
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.lib.sandbox.model.SignUp
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import sandbox.lib.Res
import sandbox.lib.check

@Adaptive
fun welcome() {
    val signUp = SignUp()

    grid {
        colTemplate(1.fr)
        rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)
        paddingLeft(32.dp)
        paddingRight(32.dp)

        titleLarge("Welcome")

        subTitle("Sign up to join")

        grid {
            colTemplate(1.fr)
            rowTemplate(52.dp repeat 4, 60.dp, 50.dp)

            input(*input) { signUp.name }
            input(*input) { signUp.email }
            input(*input) { signUp.password }
            input(*input) { signUp.verification }

            grid {
                paddingTop(15.dp)
                AlignItems.start
                colTemplate(40.dp, 1.fr)

                checkbox { signUp.agreement }
                row {
                    text("I agree to the ", fontSize(15.sp), FontName("Noto Sans"), textColor(mediumGray))
                    text("Terms of Service", fontSize(15.sp), FontName("Noto Sans"), textColor(black), bold, externalLink("/terms.txt"))
                }
            }

            button("Sign Up", onClick { println("sing up") }) .. maxWidth
        }

        footerLink("Have an account? ", "Sign in", "/")
    }

}

@Adaptive
fun titleLarge(text: String) {
    row(height(213.dp), paddingTop(117.dp)) {
        text(text, *titleLarge)
    }
}

@Adaptive
fun subTitle(text: String) {
    text(text, *bodyMedium, FontWeight.LIGHT, paddingTop(15.dp))
}

@Adaptive
fun footerLink(normalText: String, linkText: String, href: String) {
    row(AlignItems.center, maxWidth) {
        text(normalText, *bodyMedium)
        text(linkText, FontSize(17.sp), FontName("Noto Sans"), textColor(black), underline, externalLink(href))
    }
}

/**
 * Editor for a boolean.
 */
@Adaptive
fun checkbox(
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
    selector: () -> Boolean
) {
    checkNotNull(binding)

    row {
        onClick { binding.setValue(! binding.value, true) }

        if (binding.value) {
            box(*activeCheckBox) {
                image(Res.drawable.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(*inactiveCheckBox) {

            }
        }
    }
}

val titleLarge = instructionsOf(
    FontSize(40.sp),
    FontWeight.BOLD
)

val bodyMedium = instructionsOf(
    //FontSize(17.sp),
    //FontName("Noto Sans"),
    textColor(0x666666u)
)

val input = instructionsOf(
    // PlaceholderColor(0x8A8A8F),
    textColor(0x000000u),
    BackgroundColor(Color(0xEFEFF4u)),
    CornerRadius(8.dp),
    Border.NONE,
    Height(44.dp),
    FontSize(17.sp),
    FontWeight.LIGHT,
    padding(left = 16.dp, right = 16.dp)
)

var activeCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    backgroundColor(purple),
    textColor(white)
)

var inactiveCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    border(purple, 1.dp)
)