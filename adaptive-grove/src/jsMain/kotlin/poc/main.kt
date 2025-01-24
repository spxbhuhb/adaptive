package poc/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import adaptive_grove.generated.resources.commonMainStringsStringStore0
import adaptive_grove.generated.resources.components
import adaptive_grove.generated.resources.instructions
import adaptive_grove.generated.resources.palette
import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveFragmentFactory
import `fun`.adaptive.grove.api.hydrated
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
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

            val viewModel = UfdViewModel()

            grid {
                colTemplate(200.dp, 200.dp, 1.fr, 200.dp)
                maxSize

                palette(viewModel)
                descendantsList(viewModel)
                sheet(viewModel)
                instructions(viewModel)
            }
        }
    }
}

@Adaptive
fun palette(viewModel: UfdViewModel) {
    val items = autoCollection(viewModel.palette) ?: emptyList()

    column {
        maxSize .. borderRight(colors.outline)

        areaTitle(Strings.palette)

        for (item in items) {
            draggable {
                transferData { LfmDescendant(UUID<LfmDescendant>(), key = item.key, emptyList()) }
                text(item.key)
            }
        }
    }
}

@Adaptive
fun descendantsList(viewModel: UfdViewModel) {
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
fun sheet(viewModel: UfdViewModel) {
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
fun instructions(viewModel: UfdViewModel) {
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