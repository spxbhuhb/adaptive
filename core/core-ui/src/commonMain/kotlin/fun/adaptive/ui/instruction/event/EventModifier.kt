package `fun`.adaptive.ui.instruction.event

import `fun`.adaptive.runtime.GlobalRuntimeContext

enum class EventModifier {
    SHIFT, CTRL, ALT, META, DOUBLE;

    val isMultiSelect
        get() = if (GlobalRuntimeContext.platform.isMac) {
            this == META
        } else {
            this == CTRL
        }

    val isUiCtrl
        get() = if (GlobalRuntimeContext.platform.isMac) {
            this == META
        } else {
            this == CTRL
        }
}