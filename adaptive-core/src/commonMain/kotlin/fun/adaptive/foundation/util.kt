package `fun`.adaptive.foundation

fun AdaptiveAdapter.dumpFragmentTree(): String =
    rootFragment.dumpFragmentTree()

fun AdaptiveFragment.dumpFragmentTree(indent: String = ""): String {
    val lines = mutableListOf<String>()
    lines += indent + this.toString()
    for (child in children) {
        lines += child.dumpFragmentTree("$indent  ")
    }
    return lines.joinToString("\n")
}