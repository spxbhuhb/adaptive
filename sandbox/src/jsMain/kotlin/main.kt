/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.validate
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.traceAll
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveFragmentFactory
import `fun`.adaptive.grove.api.hydrated
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.sandbox.components
import `fun`.adaptive.sandbox.instructions
import `fun`.adaptive.sandbox.palette
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Adat
class Snapshot(
    val before: LfmFragment,
    val operations: List<String>
)

@Adat
class LfmSelection(
    val selected: List<UUID<LfmDescendant>>
)

val emptySelection = LfmSelection(emptyList())

class FdiViewModel {
    val components = autoCollectionOrigin(emptyList<LfmDescendant>())
    val snapshots = autoCollectionOrigin(emptyList<Snapshot>())
    val selection = autoItemOrigin(emptySelection)
    val target = autoItemOrigin(emptySelection)

    fun addDescendant(event: UIEvent) {
        val transfer = (event.transferData?.data as? LfmDescendant) ?: return

        components += LfmDescendant(
            UUID<LfmDescendant>(),
            transfer.key,
            listOf(
                LfmMapping(
                    dependencyMask = 0,
                    LfmConst("T", "Hello World!")
                ),
                LfmMapping(
                    dependencyMask = 0,
                    LfmConst(
                        "Lfun.adaptive.foundation.instruction.AdaptiveInstructionGroup;",
                        AdaptiveInstructionGroup(
                            listOf(
                                Position(event.y.dp, event.x.dp)
                            )
                        )
                    )
                )
            )
        )
    }

    fun select(reference: UUID<LfmDescendant>, additional: Boolean) {
        if (additional) {
            selection.update(LfmSelection(selection.value.selected + reference))
        } else {
            selection.update(LfmSelection(listOf(reference)))
        }
    }

    fun clearSelection() {
        selection.update(emptySelection)
    }
}

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        browser(CanvasFragmentFactory, SvgFragmentFactory, GroveFragmentFactory, backend = backend { }) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val viewModel = FdiViewModel()

            grid {
                colTemplate(200.dp, 200.dp, 1.fr, 200.dp)
                maxSize

                palette()
                descendantsList(viewModel)
                sheet(viewModel)
                instructions(viewModel)
            }
        }
    }
}

@Adaptive
fun palette() {
    column {
        maxSize .. borderRight(colors.outline)

        areaTitle(Strings.palette)

        draggable {
            transferData { LfmDescendant(UUID<LfmDescendant>(), key = "aui:text", emptyList()) }
            text("Text")
        }
    }
}

@Adaptive
fun descendantsList(viewModel: FdiViewModel) {
    val descendants = autoCollection(viewModel.components) ?: emptyList()

    column {
        maxSize .. borderRight(colors.outline)

        areaTitle(Strings.components)

        for (item in descendants) {
            text(item.key)
        }
    }
}

@Adaptive
fun sheet(viewModel: FdiViewModel) {
    val descendants = autoCollection(viewModel.components) ?: emptyList()

    dropTarget {
        onDrop { viewModel.addDescendant(it) }

        box {
            maxSize
            for (item in descendants) {
                hydrated(
                    LfmFragment(emptyList(), emptyList(), listOf(item)),
                    item.mapping.first().mapping.value,
                    parseInstructions(item)
                ) // .. onClick { viewModel.select(item.uuid, false) }
            }
        }
    }

}

fun parseInstructions(item: LfmDescendant): AdaptiveInstructionGroup {
    return AdaptiveInstructionGroup(listOf(item.mapping.first { it.mapping.value is AdaptiveInstruction }.mapping.value as AdaptiveInstruction))
}

@Adaptive
fun instructions(viewModel: FdiViewModel) {
    val allDescendants = autoCollection(viewModel.components) ?: emptyList()
    val selection = autoItem(viewModel.selection) ?: emptySelection

    val descendants = allDescendants.filter { it.uuid in selection.selected }

    column {
        maxSize .. borderLeft(colors.outline)

        areaTitle(Strings.instructions)

        for (item in descendants) {
            text(item.key)
        }
    }
}

@Adaptive
fun areaTitle(title: String) {
    text(title) .. maxWidth .. borderBottom(colors.outline)
}