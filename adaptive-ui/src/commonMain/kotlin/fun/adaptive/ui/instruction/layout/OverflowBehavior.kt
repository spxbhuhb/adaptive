package `fun`.adaptive.ui.instruction.layout

enum class OverflowBehavior(
    val value: String
) {
    Visible("visible"),
    Hidden("hidden"),
    Auto("auto"),
    Scroll("scroll"),
    Clip("clip");
}