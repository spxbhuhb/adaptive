package `fun`.adaptive.iot.space

import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.cookbook.add
import `fun`.adaptive.document.ui.direct.h1
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.sandbox.*
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.collapse_all
import `fun`.adaptive.ui.builtin.expand_all
import `fun`.adaptive.ui.builtin.remove
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.splitpane.splitPaneDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.utility.UUID

typealias ProjectItemId = String

val splitConfiguration =
    SplitPaneConfiguration(
        SplitVisibility.Both,
        SplitMethod.FixFirst,
        300.0,
        Orientation.Horizontal
    )

@Adaptive
fun treeMain(): AdaptiveFragment {

    val config = copyOf { splitConfiguration }

    val treeViewModel = TreeViewModel(
        staticTree,
        selectedFun = ::defaultSelectedFun,
        multiSelect = false
    )

    localContext(treeViewModel) {
        box {
            maxSize .. backgrounds.friendlyOpaque .. padding { 16.dp } .. borders.outline
            splitPane(
                config,
                { spaceTree(treeViewModel) },
                { splitPaneDivider() },
                { spaceEditor(treeViewModel) }
            )
        }
    }

    return fragment()
}

@Adaptive
fun spaceTree(treeViewModel: TreeViewModel<Space>): AdaptiveFragment {

    val observed = valueFrom { treeViewModel }

    column {
        maxSize .. overflow.hidden .. verticalScroll .. backgrounds.surfaceVariant

        row {
            maxWidth .. spaceBetween .. alignItems.center

            text(Strings.areas)

            row {
                actionIcon(Graphics.expand_all, theme = denseIconTheme) .. onClick { observed.items.forEach { it.expandAll() } }
                actionIcon(Graphics.collapse_all, theme = denseIconTheme) .. onClick { observed.items.forEach { it.collapseAll() } }
                box {
                    size(24.dp)

                    actionIcon(Graphics.add, theme = denseIconTheme)

                    primaryPopup { hide ->
                        contextMenu(addTopMenu) { menuItem, _ -> apply(treeViewModel, menuItem, null); hide() }
                    }
                }
            }

        }

        column {
            maxSize .. padding { 16.dp }
            tree(observed, ::contextMenuBuilder)
        }
    }

    return fragment()
}

@Adaptive
fun spaceEditor(treeViewModel: TreeViewModel<Space>): AdaptiveFragment {
    val observed = valueFrom { treeViewModel }

    val item = observed.selection.firstOrNull()

    column {
        maxSize

        if (item == null) {
            text(Strings.selectArea) .. alignSelf.center .. textColors.onSurfaceVariant
        } else {
            areaEditor(item)
        }
    }

    return fragment()
}

@Adaptive
fun areaEditor(item: TreeItem<Space>) {
    val observed = valueFrom { item }
    val space = observed.data

    column {
        maxSize .. verticalScroll .. padding { 16.dp }

        h1(space.name)
        text(space.uuid) .. textColors.onSurfaceVariant .. textSmall

        row {
            text("name:")
            input(value = space.name) {
                item.title = it
                observed.data = space.copy(name = it)
            } .. height(36.dp)
        }
    }
}

fun apply(tree: TreeViewModel<Space>, menuItem: MenuItem<SpaceEditOperation>, treeItem: TreeItem<Space>?) {
    val space : Space

    when (menuItem.data) {
        SpaceEditOperation.AddSite -> {
            space = Space(
                uuid = UUID(),
                piid = ProjectItemId(),
                type = SpaceType.Site,
                name = "${Strings.site} ${tree.items.size + 1}"
            )
        }

        SpaceEditOperation.AddBuilding -> {
            space = Space(
                uuid = UUID(),
                piid = ProjectItemId(),
                type = SpaceType.Building,
                name = "${Strings.building} ${tree.items.size + 1}"
            )
        }

        SpaceEditOperation.AddFloor -> {
            space = Space(
                uuid = UUID(),
                piid = ProjectItemId(),
                type = SpaceType.Floor,
                name = "${Strings.floor} ${tree.items.size + 1}"
            )
        }

        SpaceEditOperation.AddRoom -> {
            space = Space(
                uuid = UUID(),
                piid = ProjectItemId(),
                type = SpaceType.Room,
                name = "${Strings.room} ${tree.items.size + 1}"
            )
        }

        SpaceEditOperation.AddArea -> {
            space = Space(
                uuid = UUID(),
                piid = ProjectItemId(),
                type = SpaceType.Area,
                name = "${Strings.area} ${tree.items.size + 1}"
            )
        }

        SpaceEditOperation.Inactivate -> TODO()
    }

    val newItem = space.toTreeItem(treeItem)

    if (treeItem != null) {
        treeItem.children += newItem
        if (! treeItem.open) treeItem.open = true
    } else {
        tree.items += newItem
    }
}

val addTopMenu = listOf(
    MenuItem<SpaceEditOperation>(Graphics.responsive_layout, Strings.addSite, SpaceEditOperation.AddSite),
    MenuItem<SpaceEditOperation>(Graphics.apartment, Strings.addBuilding, SpaceEditOperation.AddBuilding),
    MenuItem<SpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, SpaceEditOperation.AddArea),
)

val siteMenu = listOf(
    MenuItem<SpaceEditOperation>(Graphics.apartment, Strings.addBuilding, SpaceEditOperation.AddBuilding),
    MenuItem<SpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, SpaceEditOperation.AddArea),
    MenuItem<SpaceEditOperation>(Graphics.remove, Strings.inactivate, SpaceEditOperation.Inactivate),
)

val buildingMenu = listOf(
    MenuItem<SpaceEditOperation>(Graphics.stacks, Strings.addFloor, SpaceEditOperation.AddFloor),
    MenuItem<SpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, SpaceEditOperation.AddArea),
    MenuItem<SpaceEditOperation>(Graphics.remove, Strings.inactivate, SpaceEditOperation.Inactivate),
)

val floorMenu = listOf(
    MenuItem<SpaceEditOperation>(Graphics.meeting_room, Strings.addRoom, SpaceEditOperation.AddRoom),
    MenuItem<SpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, SpaceEditOperation.AddArea),
    MenuItem<SpaceEditOperation>(Graphics.remove, Strings.inactivate, SpaceEditOperation.Inactivate),
)

val roomMenu = listOf(
    MenuItem<SpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, SpaceEditOperation.AddArea),
    MenuItem<SpaceEditOperation>(Graphics.remove, Strings.inactivate, SpaceEditOperation.Inactivate),
)

fun menu(space: Space) =
    when (space.type) {
        SpaceType.Site -> siteMenu
        SpaceType.Building -> buildingMenu
        SpaceType.Floor -> floorMenu
        SpaceType.Room -> roomMenu
        SpaceType.Area -> roomMenu
    }

@Adaptive
fun contextMenuBuilder(
    hide: () -> Unit,
    treeItem: TreeItem<Space>
) {
    val viewModel = fragment().firstContext<TreeViewModel<Space>>()

    column {
        zIndex { 200 }
        contextMenu(menu(treeItem.data)) { menuItem, _ -> apply(viewModel, menuItem, treeItem); hide() }
    }
}

val staticTree = emptyList<TreeItem<Space>>()