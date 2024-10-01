package `fun`.adaptive.cookbook.ui.grid

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun gridAlignRecipe() {
    flowBox {
        padding { 20.dp } .. gap {  20.dp }
        recipeGrid("alignItems.center", alignItems.center)
        recipeGrid("alignItems.top", alignItems.top)
        recipeGrid("alignItems.topCenter", alignItems.topCenter)
    }
}

@Adaptive
private fun recipeGrid(title : String, align: AlignItems) {
    column {
        backgroundColor(colors.surfaceVariant)

        text(title) .. alignSelf.center
        grid {
            size(200.dp, 200.dp)
            align .. borders.outline
            colTemplate(100.dp, 100.dp)
            rowTemplate(100.dp, 100.dp)

            box { size(80.dp, 80.dp) .. backgroundColor(0xff0000) }
            box { size(80.dp, 80.dp) .. backgroundColor(0xffff00) }
            box { size(80.dp, 80.dp) .. backgroundColor(0xff00ff) }
            box { size(80.dp, 80.dp) .. backgroundColor(0x0000ff) }
        }
    }
}