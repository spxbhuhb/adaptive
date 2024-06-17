/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package old

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.sandbox.api.SignUp
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import sandbox.Res
import sandbox.check

@Adaptive
fun welcome() {
    val signUp = SignUp()

    grid(
        colTemplate(1.fr),
        rowTemplate(213.dp, 78.dp, 1.fr, 81.dp),
        paddingLeft(32.dp),
        paddingRight(32.dp),
    ) {
        titleLarge("Welcome")

        subTitle("Sign up to join")

        grid(
            colTemplate(1.fr),
            rowTemplate(replicate(4, 52.dp), 60.dp, 50.dp)
        ) {
            input(*input) { signUp.name }
            input(*input) { signUp.email }
            input(*input) { signUp.password }
            input(*input) { signUp.verification }

            row(paddingTop(15.dp)) {
                checkbox { signUp.agreement }
                text("I agree to the", FontSize(15.sp), mediumGray, spaceAround)
                text("Terms of Service", FontSize(15.sp), black, bold, externalLink("/terms.txt"))
            }

            button("Sign Up", onClick { println("sing up") })
        }

        footerLink("Have an account?", "Sign in", "/")
    }
}

@Adaptive
fun titleLarge(text : String) {
    row(height(213.dp), paddingTop(117.dp)) {
        text(text, *titleLarge)
    }
}

@Adaptive
fun subTitle(text : String) {
    text(text, *bodyMedium, FontWeight.LIGHT, paddingTop(15.dp))
}

@Adaptive
fun footerLink(normalText : String, linkText : String, href : String) {
    row(JustifyContent.Center) {
        text(normalText, *bodyMedium, spaceAfter)
        text(linkText, FontSize(17.sp), black, TextDecoration.Underline, externalLink(href))
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

    row(onClick { binding.setValue(!binding.value, true) }) {
        if (binding.value) {
            box(*activeCheckBox) {
                image(Res.drawable.check, frame(1.dp, 1.dp, 18.dp, 18.dp))
            }
        } else {
            box(*inactiveCheckBox) {

            }
        }
    }
}

val spaceAround = padding(left = 6.dp, right = 6.dp)
val spaceAfter = paddingRight(6.dp)

val titleLarge = arrayOf(
    FontSize(40.sp),
    FontWeight.BOLD
)

val bodyMedium = arrayOf(
    FontSize(17.sp),
    Color(0x666666u)
)

val input = arrayOf(
    // PlaceholderColor(0x8A8A8F),
    Color(0x000000u),
    BackgroundColor(Color(0xEFEFF4u)),
    BorderRadius(8.dp),
    Border.NONE,
    Height(44.dp),
    FontSize(17.sp),
    FontWeight.LIGHT,
    padding(left = 16.dp, right = 16.dp)
)

var activeCheckBox = arrayOf(
    size(20.dp,20.dp),
    borderRadius(10.dp),
    backgroundColor(purple),
    white
)

var inactiveCheckBox = arrayOf(
    size(20.dp,20.dp),
    borderRadius(10.dp),
    border(purple, 1.dp)
)