/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.instruction.trace
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.resource.image.Images
import `fun`.adaptive.site.*
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.platform.media.MediaMetrics

val clientBackend = backend {  }

fun main() {

    //withJsonWebSocketTransport(window.location.origin)

    browser(CanvasFragmentFactory, SvgFragmentFactory, backend = clientBackend) { adapter ->
        with(adapter.defaultTextRenderData) {
            fontName = "Noto Sans"
            fontSize = 16.sp
        }

        val media = mediaMetrics()
        val background = if (media.isLight) lightBackground else darkBackground

        grid {
            maxSize .. rowTemplate(48.dp, 1.fr) .. background

            header(media)
            content(media)
        }
    }
}

@Adaptive
fun header(media: MediaMetrics) {
    row {
        maxWidth .. height { 48.dp } .. spaceBetween .. alignItems.center .. darkBackground
        paddingLeft { 16.dp } .. paddingRight { 24.dp }
        fixed .. zIndex { 1 }

        box {
            width { 24.dp } .. height { 24.dp }
            onClick { }
            image(Images.menu)
        }

        text("Adaptive", textColor(white))
        text(if (media.isLight) "dark" else "light", textColor(white)) .. onClick { adapter().switchTheme() } .. noSelect
    }
}

@Adaptive
fun content(media: MediaMetrics) {
    val background = if (media.isLight) lightBackground else darkBackground
    val textColor = if (media.isLight) darkTextColor else lightTextColor

    column {
        maxWidth .. paddingTop { 32.dp } .. background .. trace(".*")

        slot(mainContent) { cards() }

        text("adaptive.fun does not use cookies") ..
            paddingTop { 32.dp } ..
            paddingBottom { 32.dp } ..
            alignSelf.center ..
            textColor
    }
}

@Adaptive
fun cards() {
    row {
        maxWidth .. alignItems.topCenter

        flowBox {
            flowItemLimit { 4 }
            gap(24.dp)

            card(Images.what_is_adaptive, color(0xFFF7E1u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/what-is-adaptive.md")
                    largeTitle("What is Adaptive")
                    mediumTitle("main concept")
                    mediumTitle("features")
                    mediumTitle("goals")
                }
            }

            card(Images.preview_status, color(0xFF6347u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/status.md")
                    largeTitle("Status")
                    mediumTitle("preview")
                    mediumTitle("web, Android, iOS - PoC works")
                    mediumTitle("roadmap")
                }
            }

            card(Images.getting_started, color(0x87CEFAu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/getting-started.md")
                    largeTitle("Getting Started")
                    mediumTitle("demo")
                    mediumTitle("tutorial")
                    mediumTitle("documentation")
                }
            }

            card(Images.tools, color(0xFFB07Cu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/tools.md")
                    largeTitle("Tools")
                    inactiveFeature("project wizard", "2024.08")
                    inactiveFeature("UI designer", "2024.10")
                    inactiveFeature("AI integration", "Christmas")
                }
            }

            card(Images.ui, color(0xFFBF00u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/ui/README.md")
                    largeTitle("User Interface")
                    mediumTitle("multiplatform")
                    mediumTitle("reactive")
                    mediumTitle("pretty")
                }
            }

            card(Images.server, color(0x3CB371u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/server/README.md")
                    largeTitle("Server & Cloud")
                    mediumTitle("multiplatform")
                    mediumTitle("reactive")
                    mediumTitle("API driven")
                }
            }

            card(Images.impressum, color(0xAEE1E1u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/impressum.md")
                    largeTitle("Impressum")
                    mediumTitle("motivation")
                    mediumTitle("credits")
                    mediumTitle("license")
                }
            }

            card(Images.deep_waters, color(0x6495EDu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/internals")
                    largeTitle("Deep Waters")
                    mediumTitle("internals")
                    mediumTitle("compiler plugin")
                    mediumTitle("source code")
                }
            }
        }
    }
}

@Adaptive
fun card(
    image: ImageResourceSet,
    background: Color,
    @Adaptive cardContent: () -> Unit
) {
    val media = mediaMetrics()
    val shadowIfLight = if (media.isLight) arrayOf(shadow) else emptyArray()

    column(*shadowIfLight) {
        grid {
            size(360.dp, 214.dp) .. padding(32.dp) .. cornerTopRadius(16.dp) .. backgroundColor(background)

            cardContent()
        }

        image(image) .. size(360.dp, (360 * 1024 / 1792).dp) .. cornerBottomRadius(16.dp)
    }
}

@Adaptive
fun largeTitle(content: String) {
    text(content, titleLarge, paddingBottom(16.dp))
}

@Adaptive
fun mediumTitle(content: String) {
    text(content, titleMedium)
}

@Adaptive
fun inactiveFeature(name: String, eta: String) {
    row {
        maxWidth .. spaceBetween
        gap(10.dp)

        text(name, titleMedium, textColor(gray), noTextWrap)
        text("ETA: $eta", titleSmall, textColor(gray), noTextWrap) .. AlignSelf.Companion.bottom
    }
}