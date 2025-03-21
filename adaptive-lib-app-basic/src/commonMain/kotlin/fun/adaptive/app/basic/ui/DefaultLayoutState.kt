package `fun`.adaptive.app.basic.ui

import `fun`.adaptive.adat.Adat

@Adat
class DefaultLayoutState(
    val smallMode: SidebarUserMode = SidebarUserMode.Closed,
    val mediumMode: SidebarUserMode = SidebarUserMode.Closed,
    val largeMode: SidebarUserMode = SidebarUserMode.Open
)