package `fun`.adaptive.value.util

import `fun`.adaptive.value.AvValue

class PathAndValue<SPEC>(
    val path: List<String>,
    val value: AvValue<SPEC>
)