package `fun`.adaptive.sandbox.recipe.ui.tree

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.cookbook.generated.resources.lock
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.support.configureForm
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.editor.intEditor
import `fun`.adaptive.ui.editor.selectEditorList
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.generated.resources.account_circle
import `fun`.adaptive.ui.generated.resources.folder
import `fun`.adaptive.ui.input.select.item.selectInputOptionCheckbox
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewBackend
import `fun`.adaptive.ui.tree.TreeViewBackend.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
import kotlin.random.Random

@Adaptive
fun treePlayground(): AdaptiveFragment {

    val form = valueFrom { adatFormBackend(TreePlaygroundConfig()) }

    flowBox {
        gap { 16.dp }
        treePlaygroundForm(form)
        treePlaygroundResult(form.inputValue)
    }

    return fragment()
}

val icons = listOf("folder", "lock", "account_circle", "none")

@Adat
class TreePlaygroundConfig(
    val icon: String? = icons.first(),
    val singleClickOpen: Boolean = true,
    val doubleClickOpen: Boolean = false,
    val randomData: Boolean = false,
    val matchBackground: Boolean = false,
    val randomDataDepth: Int = 3,
    val width: Double? = 160.0,
    val height: Double? = 160.0
)

@Adaptive
fun treePlaygroundForm(
    form: AdatFormViewBackend<TreePlaygroundConfig>
) {

    val template = TreePlaygroundConfig()

    configureForm(form) {
        column {
            width { 288.dp } .. gap { 16.dp } .. padding { 16.dp }

            row {
                gap { 16.dp }
                doubleEditor { template.width } .. width { 120.dp }
                doubleEditor { template.height } .. width { 120.dp }
            }

            column { // this column does not use gap, so boolean options look nice
                maxWidth
                booleanEditor { template.singleClickOpen }
                booleanEditor { template.doubleClickOpen }
                booleanEditor { template.matchBackground }
                booleanEditor { template.randomData }
            }

            intEditor { template.randomDataDepth } .. width { 120.dp }

            selectEditorList(icons, { selectInputOptionCheckbox(it) }) { template.icon }
        }
    }
}

@Adaptive
fun treePlaygroundResult(config: TreePlaygroundConfig) {

    val icon = when (config.icon) {
        "lock" -> Graphics.lock
        "folder" -> Graphics.folder
        "account_circle" -> Graphics.account_circle
        else -> null
    }

    val width: AdaptiveInstruction = if (config.width != null) width { config.width.dp } else nop
    val height: AdaptiveInstruction = if (config.height != null) height { config.height.dp } else nop
    val background : AdaptiveInstruction = if (config.matchBackground) backgrounds.surfaceVariant else nop

    val backend =
        TreeViewBackend(
            if (config.randomData) generate(config.randomDataDepth, icon) else staticTree(icon),
            context = Unit,
            selectedFun = ::defaultSelectedFun,
            singleClickOpen = config.singleClickOpen,
            doubleClickOpen = config.doubleClickOpen
        )

    column {
        borders.outline .. verticalScroll .. width .. height .. background
        tree(backend)
    }
}


private fun generate(depth: Int, icon: GraphicsResourceSet?): List<TreeItem<Unit>> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it + 1, depth, null, icon) } // Adjust depth as needed
}

private fun generateRandomTree(index: Int, depth: Int, parent: TreeItem<Unit>?, icon: GraphicsResourceSet?): TreeItem<Unit> {
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(1, 4)

    val item = TreeItem(
        icon = icon,
        title = nodeTitle,
        data = Unit,
        parent = parent
    )

    item.children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it + 1, depth - 1, item, icon) }
    }

    return item
}