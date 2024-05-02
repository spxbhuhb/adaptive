/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.base.producer.poll
import hu.simplexion.adaptive.css.display_flex
import hu.simplexion.adaptive.css.flex_direction_column
import hu.simplexion.adaptive.css.width_240_px
import hu.simplexion.adaptive.dom.AdaptiveDOMAdapter
import hu.simplexion.adaptive.email.api.EmailApi
import hu.simplexion.adaptive.html.button
import hu.simplexion.adaptive.html.div
import hu.simplexion.adaptive.html.launchButton
import hu.simplexion.adaptive.html.text
import hu.simplexion.adaptive.service.getService
import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

fun main() {
    adaptive(AdaptiveDOMAdapter(window.document.body !!)) {

        var counter = 0
        val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }

        div(display_flex, flex_direction_column, width_240_px) {
            div { text("incremented $counter times(s)") }

            div { text(time.toString()) }

            button("Click to increment!") {
                counter = counter + 1
            }

            launchButton("Send email!") {
                getService<EmailApi>().send(
                    recipients = "noreply@example.org",
                    subject = "Test Email",
                    contentText = "Hello!\n\nTime is: $time\nCounter is at $counter\n\nHave a nice day!"
                )
            }
        }
    }
}