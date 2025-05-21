package `fun`.adaptive.ui.support.snapshot

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.wireformat.builtin.PolymorphicWireFormat

fun AbstractAuiAdapter<*, *>.snapshot(): FragmentSnapshot {
    return rootFragment.snapshot()
}

fun AdaptiveFragment.snapshot(
    uiOnly: Boolean = true,
    skipStructural: Boolean = true
): FragmentSnapshot {
    val renderData = if (this is AbstractAuiFragment<*>) renderData else null

    return FragmentSnapshot(
        this.toKey(),
        state.toList().map { it.polymorphicOrToString() },
        children.mapNotNull {
            if (uiOnly && it !is AbstractAuiFragment<*>) return@mapNotNull null
            if (skipStructural && it is AbstractAuiFragment<*> && it.isStructural) return@mapNotNull null
            it.snapshot(uiOnly, skipStructural)
        },
        renderData?.finalTop,
        renderData?.finalLeft,
        renderData?.finalWidth,
        renderData?.finalHeight
    )
}

// FIXME polymorphicOrToString converts null values to null string as polymorphic list throws exception on null values
// FIXME should we make BoundFragmentFactory and producers Adat compatible?
fun Any?.polymorphicOrToString() =
    when {
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
