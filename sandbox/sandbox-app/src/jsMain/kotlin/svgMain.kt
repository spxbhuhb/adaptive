import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun svgMain() {
    box {
        size(100.dp, 100.dp) .. margin { 16.dp } .. borders.friendly

        svg(Graphics.folder) .. fill(0x0000ff) .. svgWidth(12.dp)
    }
}