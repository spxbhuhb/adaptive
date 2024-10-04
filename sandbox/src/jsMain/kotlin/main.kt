/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.lib.sandbox.withSandbox
import `fun`.adaptive.service.transport.LocalServiceCallTransport
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.platform.withJsResources
import `fun`.adaptive.ui.theme.iconColors
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds


fun main() {

    withJsResources()

    browser(backend = backend(LocalServiceCallTransport()) { }) {
        withSandbox(it) // to set default font name
        val random = poll(1.seconds) { now().epochSeconds.toInt() % 4 } ?: 0
        val colors = arrayOf(iconColors.onSurface, iconColors.onSurfaceVariant, iconColors.onSurfaceFriendly, iconColors.onSurfaceAngry)

        row {
            padding { 16.dp } .. width { 400.dp }
            svg(Res.drawable.check) .. colors[random] .. width { 40.dp }
            text(" - with changing color - $random")
        }
    }
}
