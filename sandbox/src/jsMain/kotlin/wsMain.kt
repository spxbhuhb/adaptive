import `fun`.adaptive.cookbook.recipe.ui.layout.workspace.workspaceRecipe
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun wsMain() {
    box {
        padding { 16.dp } .. borders.friendly .. maxSize
        workspaceRecipe()
    }
}