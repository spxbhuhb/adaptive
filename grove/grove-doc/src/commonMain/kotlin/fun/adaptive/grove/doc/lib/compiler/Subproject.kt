package `fun`.adaptive.grove.doc.lib.compiler

import kotlinx.io.files.Path

/**
 * Represents an included build (subproject) discovered from settings.gradle.kts.
 *
 * @param name         The subproject name (last segment of the include path)
 * @param relativePath The path as written in settings.gradle.kts includeBuild("...")
 * @param path         The resolved path relative to the compilation input directory
 */
data class Subproject(
    val name: String,
    val relativePath: String,
    val path: Path
)
