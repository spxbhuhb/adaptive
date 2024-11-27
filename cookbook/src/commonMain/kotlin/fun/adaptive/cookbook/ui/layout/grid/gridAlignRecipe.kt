package `fun`.adaptive.cookbook.ui.layout.grid

import `fun`.adaptive.cookbook.shared.blue
import `fun`.adaptive.cookbook.shared.green
import `fun`.adaptive.cookbook.shared.purple
import `fun`.adaptive.cookbook.shared.red
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
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
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

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
        backgrounds.surfaceVariant

        text(title) .. alignSelf.center
        grid {
            size(200.dp, 200.dp)
            align .. borders.outline
            colTemplate(100.dp, 100.dp)
            rowTemplate(100.dp, 100.dp)

            box { size(80.dp, 80.dp) .. backgrounds.red }
            box { size(80.dp, 80.dp) .. backgrounds.green }
            box { size(80.dp, 80.dp) .. backgrounds.purple }
            box { size(80.dp, 80.dp) .. backgrounds.blue }
        }
    }
}