package `fun`.adaptive.cookbook.ui.text

import `fun`.adaptive.cookbook.shared.cornerRadius4
import `fun`.adaptive.cookbook.shared.cornerRadius8
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun textRecipe() {
    column {
        gap { 16.dp } .. maxWidth

        text("default")
        text("with color") .. textColors.onSurfaceFriendly
        colorChange()
        contentChange()
    }
}

@Adaptive
private fun colorChange() {
    val random = poll(1.seconds) { now().epochSeconds.toInt() % 4 } ?: 0
    val colors = arrayOf(textColors.onSurface, textColors.onSurfaceVariant, textColors.onSurfaceFriendly, textColors.onSurfaceAngry)

    text("with changing color - $random") .. colors[random]
}

@Adaptive
private fun contentChange() {
    val random = poll(1.seconds) { now().epochSeconds.toInt() % 4 } ?: 0
    val contents = arrayOf("short", "long", "very long", "extremely long")

    grid {
        colTemplate(100.dp, 100.dp)
        rowTemplate(40.dp repeat 5)
        gap { 16.dp }

        text("column")
        column {
            borders.outline .. size(100.dp, 40.dp) .. alignItems.center .. cornerRadius8
            text(contents[random])
        }

        text("row")
        row {
            borders.outline .. size(100.dp, 40.dp) .. alignItems.center .. cornerRadius8
            text(contents[random])
        }

        text("grid")
        grid {
            borders.outline .. size(100.dp, 40.dp) .. alignItems.center .. cornerRadius8
            text(contents[random])
        }

        text("box")
        box {
            borders.outline .. size(100.dp, 40.dp) .. alignItems.center .. cornerRadius8
            text(contents[random])
        }

        text("flowBox")
        flowBox {
            borders.outline .. size(100.dp, 40.dp) .. alignItems.center .. cornerRadius8
            text(contents[random])
        }

    }
}