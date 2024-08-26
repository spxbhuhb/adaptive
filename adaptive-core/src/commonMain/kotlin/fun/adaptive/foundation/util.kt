package `fun`.adaptive.foundation

fun AdaptiveFragment.dumpFragmentTree(indent: String = ""): String {
    val lines = mutableListOf<String>()
    lines += this.toString()
    for (child in children) {
        lines += child.dumpFragmentTree("$indent  ")
    }
    return lines.joinToString("\n")
}