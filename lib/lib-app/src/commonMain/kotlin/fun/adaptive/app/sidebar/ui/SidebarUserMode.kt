package `fun`.adaptive.app.sidebar.ui

import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

/**
 * The user-selected mode of the sidebar.
 *
 * Small screens
 * * open: show sidebar **over the content**
 * * closed: do not show the sidebar
 *
 * Medium screens
 * * open: show the full sidebar **next the content**
 * * closed: show the thin sidebar **next to the content**
 *
 * Large screens:
 * * open: show the full sidebar **next to the content**
 * * closed: show the thin sidebar **next to the content**
 */
enum class SidebarUserMode {
    Open,
    Closed;

    companion object : EnumWireFormat<SidebarUserMode>(entries) {
        override val wireFormatName: String
            get() = SidebarUserMode.typeSignature().trimSignature()
    }
}