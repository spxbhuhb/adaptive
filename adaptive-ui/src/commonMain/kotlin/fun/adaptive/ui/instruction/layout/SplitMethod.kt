package `fun`.adaptive.ui.instruction.layout

enum class SplitMethod {
    /**
     * The first pane is fixed size, the second occupies the remaining space.
     */
    FixFirst,

    /**
     * The second pane is fixed, the first occupies the remaining space.
     */
    FixSecond,

    /**
     * The available space is distributed between the panes according to the split value.
     */
    Proportional
}