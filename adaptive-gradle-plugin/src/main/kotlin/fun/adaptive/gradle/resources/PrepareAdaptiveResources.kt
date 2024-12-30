/*
 * Copyright 2020-2024 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 *
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 *
 * This code has been copied from Compose Multiplatform: https://github.com/JetBrains/compose-multiplatform
 */

package `fun`.adaptive.gradle.resources

import `fun`.adaptive.gradle.internal.IdeaImportTask
import `fun`.adaptive.gradle.internal.utils.uppercaseFirstChar
import `fun`.adaptive.gradle.resources.spec.ResourceType
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.w3c.dom.Node
import org.xml.sax.SAXParseException
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory

internal fun Project.registerPrepareAdaptiveResourcesTask(
    sourceSet: KotlinSourceSet
): TaskProvider<PrepareAdaptiveResourcesTask> {

    val resDir = "${sourceSet.name}/$ADAPTIVE_RESOURCES_DIR"
    val userAdaptiveResourcesDir = project.projectDir.resolve("src/$resDir")
    val preparedAdaptiveResourcesDir = layout.buildDirectory.dir("$RES_GEN_DIR/prepared/$resDir")

    val prepareAdaptiveResourcesTask = tasks.register(
        getPrepareAdaptiveResourcesTaskName(sourceSet),
        PrepareAdaptiveResourcesTask::class.java
    ) { task ->
        task.outputDir.set(preparedAdaptiveResourcesDir)
    }

    return prepareAdaptiveResourcesTask
}

internal fun Project.getPreparedAdaptiveResourcesDir(sourceSet: KotlinSourceSet): Provider<File> = tasks
        .named(
            getPrepareAdaptiveResourcesTaskName(sourceSet),
            PrepareAdaptiveResourcesTask::class.java
        )
        .flatMap { it.outputDir.asFile }

private fun getPrepareAdaptiveResourcesTaskName(sourceSet: KotlinSourceSet) =
    "prepareAdaptiveResourcesTaskFor${sourceSet.name.uppercaseFirstChar()}"

internal abstract class PrepareAdaptiveResourcesTask : IdeaImportTask() {

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    override fun safeAction() = Unit
}