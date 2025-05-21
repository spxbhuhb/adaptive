package `fun`.adaptive.ui.support.snapshot

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.instruction.event.UIEventHandler
import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat

fun AbstractAuiAdapter<*, *>.snapshot(): FragmentSnapshot {
    return rootFragment.snapshot()
}

fun AdaptiveFragment.snapshot(): FragmentSnapshot {
    val renderData = if (this is AbstractAuiFragment<*>) renderData else null

    return FragmentSnapshot(
        this.toKey(),
        state.toList().mapIndexed { index, value -> value.polymorphicOrToString(index) },
        children.map { it.snapshot() },
        renderData?.finalTop,
        renderData?.finalLeft,
        renderData?.finalWidth,
        renderData?.finalHeight
    )
}

fun AbstractAuiFragment<*>.uiSnapshot() : FragmentSnapshot {
    return FragmentSnapshot(
        this.toKey(),
        state.toList().mapIndexed { index, value -> value.polymorphicOrToString(index) },
        emptyList(),
        renderData.finalTop,
        renderData.finalLeft,
        renderData.finalWidth,
        renderData.finalHeight
    )
}

fun AbstractContainer<*,*>.uiContainerSnapshot(): FragmentSnapshot {
    return FragmentSnapshot(
        this.toKey(),
        state.toList().mapIndexed { index, value -> value.polymorphicOrToString(index) },
        layoutItems.map { if (it is AbstractContainer<*,*>) it.uiContainerSnapshot() else it.uiSnapshot() },
        renderData.finalTop,
        renderData.finalLeft,
        renderData.finalWidth,
        renderData.finalHeight
    )
}

// FIXME polymorphicOrToString converts null values to null string as polymorphic list throws exception on null values
// FIXME should we make BoundFragmentFactory and producers Adat compatible?
fun Any?.polymorphicOrToString(index : Int) =
    when {
        index == 0 -> (this as? AdaptiveInstructionGroup)?.toMutableList()?.filter { it !is UIEventHandler } ?: "null"

        this == null -> "null"

        PolymorphicWireFormat.wireFormatOrNullFor(this) == null -> {
            this::class.simpleName ?: "null"
        }

        else -> this
    }

// FIXME root regex ending is not trivial
private val rootRegex = Regex("AdaptiveRoot[a-zA-Z0-9]+\\d+")

private fun AdaptiveFragment.toKey() =
    (this::class.simpleName ?: "null").let {
        if (it.matches(rootRegex)) "<root>" else it
    }
