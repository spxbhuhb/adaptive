/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.resource.ADAPTIVE_ARTEFACT_RESOURCE_DIRECTORY
import `fun`.adaptive.utility.asUnderscoredIdentifier
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.io.File

open class ResourcesExtension {

    /**
     * The unique identifier of the resources in the current project.
     * Uses as package for the generated code and for isolation resources in a final artefact.
     *
     * If it is empty then `{group name}.{module name}.generated.resources` will be used.
     */
    var packageOfResources: String = ""

    /**
     * The directory in the final artefact into which the puts the resource files. Default
     * is `resources`. If you change this you should also set the path resolver function in
     * `WebResourcesConfiguration` or map the paths by other means.
     */
    var artefactDirectory: String = ADAPTIVE_ARTEFACT_RESOURCE_DIRECTORY

    /**
     * Whether the generated resources accessors should be public or not.
     *
     * Default is false.
     */
    var publicAccessors: Boolean = false

    /**
     * Whether the file name may contain qualifiers. This is useful if you want to
     * put the same kind of files such as strings into one directory with names like
     * `strings-hu.xml`.
     *
     * `true` means that you can't have `-` in file names as part after the `-` would
     * be treated as a qualifier.
     *
     * Default is true.
     */
    var withFileQualifiers: Boolean = true

    /**
     * Treat resources without type qualifier as File.
     *
     * Default is true.
     */
    var withFileDefault: Boolean = true

}

internal fun Provider<ResourcesExtension>.getResourcePackage(project: Project) = map { config ->
    config.packageOfResources.takeIf { it.isNotEmpty() } ?: run {
        val groupName = project.group.toString().lowercase().asUnderscoredIdentifier()
        val moduleName = project.name.lowercase().asUnderscoredIdentifier()
        val id = if (groupName.isNotEmpty()) "$groupName.$moduleName" else moduleName
        "$id.generated.resources"
    }
}

//the dir where resources must be placed in the final artefact
internal fun Provider<ResourcesExtension>.getModuleResourcesDir(project: Project) =
    getResourcePackage(project).map { packageName -> File("${get().artefactDirectory}/$packageName") }