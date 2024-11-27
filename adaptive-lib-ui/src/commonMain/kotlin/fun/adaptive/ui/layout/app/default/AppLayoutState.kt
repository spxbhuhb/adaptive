package `fun`.adaptive.ui.layout.app.default

import `fun`.adaptive.adat.Adat

@Adat
class AppLayoutState(
    val smallMode: SidebarUserMode = SidebarUserMode.Closed,
    val mediumMode: SidebarUserMode = SidebarUserMode.Closed,
    val largeMode: SidebarUserMode = SidebarUserMode.Open
)