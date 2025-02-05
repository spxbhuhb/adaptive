package `fun`.adaptive.ui.support.statistics

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiFragment

fun AdaptiveAdapter.dumpStatistics(): String =
    rootFragment.dumpStatistics().joinToString("\n")

fun AdaptiveFragment.dumpStatistics(indent: String = "", output: MutableList<String> = mutableListOf()): List<String> {

    if (this is AbstractAuiFragment<*>) {
        output += indent + this + "    " + this.statistics.dump()
    }

    for (child in children) {
        child.dumpStatistics("$indent  ", output)
    }

    return output
}