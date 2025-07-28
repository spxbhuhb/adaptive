package `fun`.adaptive.process

expect fun runCommand(
    command: String,
    arguments: List<String> = emptyList()
): String