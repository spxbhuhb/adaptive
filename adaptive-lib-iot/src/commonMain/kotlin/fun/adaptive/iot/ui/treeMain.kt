package `fun`.adaptive.iot.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.*
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.model.project.AioProject
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.AioSpaceEditOperation
import `fun`.adaptive.iot.model.space.AioSpaceType
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.input.InputState
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.menu.MenuItemBase
import `fun`.adaptive.ui.menu.MenuSeparator
import `fun`.adaptive.ui.menu.contextMenu
import `fun`.adaptive.ui.splitpane.splitPaneDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
import `fun`.adaptive.utility.UUID

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

    val treeViewModel = TreeViewModel<AioSpace, AioProject>(
        emptyList(),
        selectedFun = ::defaultSelectedFun,
        multiSelect = false,
        context = AioProject(UUID())
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
fun spaceTree(treeViewModel: TreeViewModel<AioSpace, AioProject>): AdaptiveFragment {

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
fun spaceEditor(treeViewModel: TreeViewModel<AioSpace, AioProject>): AdaptiveFragment {
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
fun areaEditor(item: TreeItem<AioSpace>) {
    val observed = valueFrom { item }
    val space = observed.data

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        column {
            paddingBottom { 32.dp }
            h2(space.name)
            uuidLabel { space.uuid }
        }

        column {

            gap { 24.dp }

            withLabel(Strings.spxbId, InputState(disabled = true)) { state ->
                width { 400.dp }
                textInput(space.friendlyId, state) { }
            }

            withLabel(Strings.type, InputState(disabled = true)) { state ->
                width { 400.dp }
                textInput(space.spaceType.localized(), state) { }
            }

            withLabel(Strings.name) {
                width { 400.dp }
                textInput(space.name) { v ->
                    item.title = v
                    observed.data = space.copy(name = v)
                }
            }

            withLabel(Strings.note) {
                width { 400.dp }
                textInputArea(space.notes) { v ->
                    observed.data = space.copy(notes = v)
                } .. height { 400.dp }
            }
        }
    }
}

fun apply(tree: TreeViewModel<AioSpace, AioProject>, menuItem: MenuItem<AioSpaceEditOperation>, treeItem: TreeItem<AioSpace>?) {
    val projectId = tree.context.uuid
    val space: AioSpace?

    when (menuItem.data) {
        AioSpaceEditOperation.AddSite -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Site,
                name = "${Strings.site} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddBuilding -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Building,
                name = "${Strings.building} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddFloor -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Floor,
                name = "${Strings.floor} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddRoom -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Room,
                name = "${Strings.room} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.AddArea -> {
            space = AioSpace(
                uuid = UUID(),
                projectId = projectId,
                friendlyId = "",
                spaceType = AioSpaceType.Area,
                name = "${Strings.area} ${tree.items.size + 1}"
            )
        }

        AioSpaceEditOperation.MoveUp -> {
            space = null
        }

        AioSpaceEditOperation.MoveDown -> {
            space = null
        }

        AioSpaceEditOperation.Inactivate -> TODO()
    }

    if (space != null) {
        val newItem = space.toTreeItem(treeItem)

        if (treeItem != null) {
            treeItem.children += newItem
            if (! treeItem.open) treeItem.open = true
        } else {
            tree.items += newItem
        }
    }
}

val addTopMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.responsive_layout, Strings.addSite, AioSpaceEditOperation.AddSite),
    MenuItem<AioSpaceEditOperation>(Graphics.apartment, Strings.addBuilding, AioSpaceEditOperation.AddBuilding),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

val siteMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.apartment, Strings.addBuilding, AioSpaceEditOperation.AddBuilding),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

val buildingMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.stacks, Strings.addFloor, AioSpaceEditOperation.AddFloor),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

val floorMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.meeting_room, Strings.addRoom, AioSpaceEditOperation.AddRoom),
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

val roomMenu = listOf(
    MenuItem<AioSpaceEditOperation>(Graphics.crop_5_4, Strings.addArea, AioSpaceEditOperation.AddArea),
)

fun menu(viewModel: TreeViewModel<AioSpace, AioProject>, treeItem: TreeItem<AioSpace>): List<MenuItemBase<AioSpaceEditOperation>> {

    val base =
        when (treeItem.data.spaceType) {
            AioSpaceType.Site -> siteMenu
            AioSpaceType.Building -> buildingMenu
            AioSpaceType.Floor -> floorMenu
            AioSpaceType.Room -> roomMenu
            AioSpaceType.Area -> roomMenu
        }

    val out = mutableListOf<MenuItemBase<AioSpaceEditOperation>>()
    out.addAll(base)

    out += MenuSeparator<AioSpaceEditOperation>()

    out += MenuItem<AioSpaceEditOperation>(
        Graphics.arrow_drop_up, Strings.moveUp, AioSpaceEditOperation.MoveUp,
        inactive = isFirst(viewModel, treeItem)
    )

    out += MenuItem<AioSpaceEditOperation>(
        Graphics.arrow_drop_down, Strings.moveDown, AioSpaceEditOperation.MoveDown,
        inactive = isLast(viewModel, treeItem)
    )

    out += MenuSeparator<AioSpaceEditOperation>()

    out += MenuItem<AioSpaceEditOperation>(null, Strings.inactivate, AioSpaceEditOperation.Inactivate)

    return out
}

fun isFirst(viewModel: TreeViewModel<AioSpace, AioProject>, treeItem: TreeItem<AioSpace>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.first() == treeItem
    } else {
        return safeParent.children.first() == treeItem
    }
}

fun isLast(viewModel: TreeViewModel<AioSpace, AioProject>, treeItem: TreeItem<AioSpace>): Boolean {
    val safeParent = treeItem.parent
    if (safeParent == null) {
        return viewModel.items.last() == treeItem
    } else {
        return safeParent.children.last() == treeItem
    }
}

@Adaptive
fun contextMenuBuilder(
    hide: () -> Unit,
    viewModel: TreeViewModel<AioSpace, AioProject>,
    treeItem: TreeItem<AioSpace>
): AdaptiveFragment {
    column {
        zIndex { 200 }
        contextMenu(menu(viewModel, treeItem)) { menuItem, _ -> apply(viewModel, menuItem, treeItem); hide() }
    }
    return fragment()
}