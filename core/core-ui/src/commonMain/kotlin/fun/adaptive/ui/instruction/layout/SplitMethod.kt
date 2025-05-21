package `fun`.adaptive.ui.instruction.layout

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

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
    Proportional,

    /**
     * The available space is distributed as if the first pane would wrap the second.
     */
    WrapFirst,

    /**
     * The available space is distributed as if the second pane would wrap the first.
     */
    WrapSecond;

    companion object : EnumWireFormat<SplitMethod>(SplitMethod.Companion.entries) {
        override val wireFormatName: String
            get() = SplitMethod.typeSignature().trimSignature()
    }

}