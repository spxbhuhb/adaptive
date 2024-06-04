import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.producer.poll
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.row
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@Adaptive
fun stuff() {
    box {
        text("stuff")
    }
}

@Adaptive
fun counterWithTime(time: Instant) {
    val counter = poll(1000.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter", Frame(150.dp, 150.dp, 250.dp, 20.dp))
}

fun chess(size: Int, cellSize: DPixel) =
    arrayOf(ColTemplate(Repeat(size, cellSize)), RowTemplate(Repeat(size, cellSize)))

fun cellColor(r: Int, c: Int) =
    if (((r * 8) + c) % 2 == 0) white else black

@Adaptive
fun chessBoard() {
    grid(*chess(8, 20.dp)) {
        for (r in 1 .. 8) {
            for (c in 1 .. 8) {
                row(cellColor(r, c)) { text(r * 8 + c) }
            }
        }
    }
}
