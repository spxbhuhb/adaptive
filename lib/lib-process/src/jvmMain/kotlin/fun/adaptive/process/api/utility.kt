package `fun`.adaptive.process.api

fun jarCommand(jarName: String) =
    arrayOf(
        System.getProperty("java.home") + "/bin/java",
        "-jar",
        jarName
    )