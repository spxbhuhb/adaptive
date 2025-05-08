package `fun`.adaptive.supervisor.model

import `fun`.adaptive.adat.Adat

@Adat
class AppSpec(
    val workingDirectory: String,
    val command: String,
    val running: Boolean,
    val autostart: Boolean
)