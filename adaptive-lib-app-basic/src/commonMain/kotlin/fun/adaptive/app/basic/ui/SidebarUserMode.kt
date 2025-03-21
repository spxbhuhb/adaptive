package `fun`.adaptive.app.basic.ui

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
    Closed
}