/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.*
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.site.*
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.mediaMetrics
import hu.simplexion.adaptive.ui.common.platform.withJsResources
import hu.simplexion.adaptive.wireformat.withJson

val black = Color(0x000000u)
val white = Color(0xffffffu)
val gray = Color(0xc0c0c0u)

val whiteBackground = BackgroundColor(black)
val blackBackground = BackgroundColor(black)

val titleLarge = FontSize(36.sp)
val titleMedium = FontSize(20.sp)
val titleSmall = FontSize(16.sp)

val shadow = dropShadow(Color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)

fun main() {

    withJson()
    withJsResources()

    browser {
        val media = mediaMetrics()
        val background = if (media.isLight) whiteBackground else blackBackground

        grid(background) {
            colTemplate(1.fr)
            rowTemplate(48.dp, 32.dp, 1.fr)

            header()
            row { }
            column(name("hello")) {
                verticalScroll

                slot(mainContent) { cards() }
            }
        }
    }
}

val mainContent = name("main content")

fun replaceMain(
    @DetachName segment: String,
    @AdaptiveDetach content: (handler: DetachHandler) -> Unit
) = NavClick(mainContent, segment, content)

private val grid1fr = arrayOf(
    rowTemplate(1.fr),
    colTemplate(1.fr)
)

@Adaptive
fun header() {
    row {
        blackBackground
        AlignItems.center

        text("Adaptive", white)
    }
}

@Adaptive
fun hello() {
    text("Hello World!")
}

@Adaptive
fun cards() {
    val media = mediaMetrics()

    grid {
        colTemplate(1.fr, 360.dp, 360.dp, 1.fr)
        rowTemplate(replicate(4, 424.dp))
        gap(24.dp)
        width { media.viewWidth.dp }

        card(Res.drawable.what_is_adaptive, Color(0x87CEFAu), 2.gridCol) {
            column {
                largeTitle("What is Adaptive")
                mediumTitle("main concept")
                mediumTitle("features")
                mediumTitle("goals")
            }
        }

        card(Res.drawable.preview_status, Color(0xF08080u), 3.gridCol) {
            column {
                largeTitle("Status")
                mediumTitle("initial development")
                mediumTitle("preview ready")
                mediumTitle("web, Android, iOS - PoC works")
            }
        }

        card(Res.drawable.getting_started, Color(0xFFF7E1u), 2.gridCol) {
            column {
                largeTitle("Getting Started")
                mediumTitle("demo")
                mediumTitle("tutorial")
                mediumTitle("documentation")
            }
        }

        card(Res.drawable.tools, Color(0xE6E6FAu), 3.gridCol) {
            column {
                largeTitle("Tools")
                inactiveFeature("project wizard", "2024.08")
                inactiveFeature("UI designer", "2024.10")
                inactiveFeature("AI integration", "christmas")
            }
        }

        card(Res.drawable.ui, Color(0xFFDAB9u), 2.gridCol) {
            column {
                largeTitle("User Interface")
                mediumTitle("multiplatform")
                mediumTitle("reactive")
                mediumTitle("pretty")
            }
        }

        card(Res.drawable.server, Color(0xB4E7B4u), 3.gridCol) {
            column {
                largeTitle("Server & Cloud")
                mediumTitle("multiplatform")
                mediumTitle("reactive")
                mediumTitle("API driven")
            }
        }

        card(Res.drawable.impressum, Color(0xFFBF00u), 2.gridCol) {
            column {
                largeTitle("Impressum")
                mediumTitle("motivation")
                mediumTitle("credits")
                mediumTitle("license")
            }
        }

        card(Res.drawable.deep_waters, Color(0xB0C4DEu), 3.gridCol) {
            column {
                largeTitle("Deep Waters")
                mediumTitle("internals")
                mediumTitle("compiler plugin")
                mediumTitle("source code")
            }
        }
    }
}

@Adaptive
fun card(
    image: DrawableResource,
    background: Color,
    vararg instructions: AdaptiveInstruction,
    @Adaptive content: () -> Unit
) {
    val media = mediaMetrics()
    val shadowIfLight = if (media.isLight) arrayOf(shadow) else emptyArray()

    column(*shadowIfLight, *instructions) {
        grid(size(360.dp, 214.dp)) {
            grid1fr
            padding(32.dp)
            cornerTopRadius(16.dp)
            backgroundColor(background)
            content()
        }
        image(
            image,
            size(360.dp, (360 * 1024 / 1792).dp),
            cornerBottomRadius(16.dp)
        )
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
        gap(10.dp)
        AlignItems.bottom

        text(name, titleMedium, gray, TextWrap.NoWrap)
        text("ETA: $eta", titleSmall, gray, TextWrap.NoWrap)
    }
}